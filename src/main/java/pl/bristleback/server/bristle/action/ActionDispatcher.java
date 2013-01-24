package pl.bristleback.server.bristle.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorsExecutor;
import pl.bristleback.server.bristle.action.response.ResponseHelper;
import pl.bristleback.server.bristle.conf.resolver.action.ActionClassesResolver;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.message.BristleMessage;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 11:43:30 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionDispatcher {

  private ActionsContainer actionsContainer;

  @Inject
  private ResponseHelper responseHelper;

  @Inject
  private ActionClassesResolver actionClassesResolver;

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  private ActionInterceptorsExecutor interceptorPolicyExecutor;

  @PostConstruct
  public void init() {
    actionsContainer = actionClassesResolver.resolve();
  }

  public void dispatch(ActionExecutionContext context) throws Exception {
    ActionInformation action = extractAction(context);
    Object[] parameters = extractActionParameters(context, action);
    Object response = executeAction(context, action, parameters);
    sendResponse(context, action, response);
  }

  private ActionInformation extractAction(ActionExecutionContext context) {
    context.extractActionInformation();
    ActionClassInformation actionClass = actionsContainer.getActionClass(context.getActionClassName());
    ActionInformation action = actionClass.getActionToExecute(context);
    context.setAction(action);
    interceptorPolicyExecutor.executeInterceptorPolicy(action, context);

    return action;
  }

  private Object[] extractActionParameters(ActionExecutionContext context, ActionInformation action) throws Exception {
    context.setStage(ActionExecutionStage.PARAMETERS_EXTRACTION);
    return resolveActionParameters(action, context);
  }

  @SuppressWarnings("unchecked")
  private Object executeAction(ActionExecutionContext context, ActionInformation action, Object[] parameters) throws Exception {
    context.setStage(ActionExecutionStage.ACTION_EXECUTION);
    Object actionClassInstance = actionsContainer.getActionClassInstance(action.getActionClass(), springIntegration);
    return action.execute(actionClassInstance, parameters);
  }

  private void sendResponse(ActionExecutionContext context, ActionInformation action, Object response) throws Exception {
    context.setStage(ActionExecutionStage.RESPONSE_CONSTRUCTION);

    if (!action.getResponseInformation().isVoidResponse()) {
      Object serialization = action.getResponseInformation().getSerialization();
      responseHelper.sendResponse(response, serialization, context);
    }
  }

  private Object[] resolveActionParameters(ActionInformation actionInformation, ActionExecutionContext context) throws Exception {
    Object[] parameters = new Object[actionInformation.getParameters().size()];
    BristleMessage<String[]> message = context.getMessage();
    for (int i = 0; i < actionInformation.getParameters().size(); i++) {
      ActionParameterInformation parameterInfo = (ActionParameterInformation) actionInformation.getParameters().get(i);
      parameters[i] = parameterInfo.resolveParameter(message.getPayload()[i], context);
    }
    return parameters;
  }
}
