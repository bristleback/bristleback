package pl.bristleback.server.bristle.action.exception.handler;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.response.ResponseHelper;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-05 13:29:53 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public final class ActionExceptionHandlers {
  private static Logger log = Logger.getLogger(ActionExceptionHandlers.class.getName());

  @Inject
  private ResponseHelper responseHelper;

  @Inject
  private BristleSpringIntegration springIntegration;

  private Map<ActionExecutionStage, HandlersForStage> handlerGroups;

  public ActionExceptionHandlers() {
    handlerGroups = new HashMap<ActionExecutionStage, HandlersForStage>();
    for (ActionExecutionStage stage : ActionExecutionStage.values()) {
      handlerGroups.put(stage, new HandlersForStage());
    }
  }

  public void initHandlers() {
    Map<String, ActionExceptionHandler> defaultHandlers = springIntegration.getFrameworkBeansOfType(ActionExceptionHandler.class);
    addHandlers(defaultHandlers.values());
    Map<String, ActionExceptionHandler> customHandlers = springIntegration.getApplicationBeansOfType(ActionExceptionHandler.class);
    addHandlers(customHandlers.values());
  }

  private void addHandlers(Collection<ActionExceptionHandler> actionExceptionHandlers) {
    for (ActionExceptionHandler handler : actionExceptionHandlers) {
      Class handledExceptionType = (Class) ReflectionUtils.getParameterTypes(handler.getClass(), ActionExceptionHandler.class)[0];
      putHandler(handledExceptionType, handler);
    }
  }

  public ActionExceptionHandler getHandler(Exception exception, ActionExecutionStage stage) {
    return handlerGroups.get(stage).getHandler(exception.getClass());
  }

  public <T extends Exception> void putHandler(Class<T> exceptionType, ActionExceptionHandler<T> handler) {
    for (ActionExecutionStage stage : handler.getHandledStages()) {
      handlerGroups.get(stage).putHandler(exceptionType, handler);
    }
  }

  @SuppressWarnings("unchecked")
  public void handleException(Exception exception, ActionExecutionContext context) {
    try {
      ActionExceptionHandler handler = getHandler(exception, context.getStage());
      Object exceptionalResponse = handler.handleException(exception, context);
      responseHelper.sendException(exceptionalResponse, context);
    } catch (Exception e) {
      log.debug("Error while handling exception in action class, stage: " + context.getStage(), e);
    }
  }

  private final class HandlersForStage {

    private Map<Class<? extends Exception>, ActionExceptionHandler<? extends Exception>> handlers;

    private HandlersForStage() {
      handlers = new HashMap<Class<? extends Exception>, ActionExceptionHandler<? extends Exception>>();
    }

    private ActionExceptionHandler getHandler(Class exceptionClass) {
      ActionExceptionHandler handler = null;
      while (handler == null) {
        handler = handlers.get(exceptionClass);
        if (handler == null && exceptionClass == Exception.class) {
          return VoidExceptionHandler.HANDLER;
        }
        exceptionClass = exceptionClass.getSuperclass();
      }
      return handler;
    }

    private <T extends Exception> void putHandler(Class<T> exceptionType, ActionExceptionHandler<T> handler) {
      handlers.put(exceptionType, handler);
    }
  }
}
