package pl.bristleback.server.bristle.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.response.ResponseHelper;
import pl.bristleback.server.bristle.api.action.ActionInformation;
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

  @PostConstruct
  public void init() {
    actionsContainer = actionClassesResolver.resolve();
  }

  @SuppressWarnings("unchecked")
  public void dispatch(ActionExecutionContext context) throws Exception {
    context.extractActionInformation();
    ActionClassInformation actionClass = actionsContainer.getActionClass(context.getActionClassName());
    Object actionClassInstance = actionsContainer.getActionClassInstance(actionClass, springIntegration);
    ActionInformation action = actionClass.getActionToExecute(context);

    setActionExecutionStage(context, ActionExecutionStage.PARAMETERS_EXTRACTION);
    Object[] parameters = resolveActionParameters(action, context);

    setActionExecutionStage(context, ActionExecutionStage.ACTION_EXECUTION);
    Object response = action.execute(actionClassInstance, parameters);

    setActionExecutionStage(context, ActionExecutionStage.RESPONSE_CONSTRUCTION);

    if (!action.getResponseInformation().isVoidResponse()) {
      Object serialization = action.getResponseInformation().getSerialization();
      responseHelper.sendResponse(response, serialization, context);
    }
  }

  private void setActionExecutionStage(ActionExecutionContext context, ActionExecutionStage newState) {
    context.setStage(newState);
  }

  public Object[] resolveActionParameters(ActionInformation actionInformation, ActionExecutionContext context) throws Exception {
    Object[] parameters = new Object[actionInformation.getParameters().size()];
    BristleMessage<String[]> message = context.getMessage();
    for (int i = 0; i < actionInformation.getParameters().size(); i++) {
      ActionParameterInformation parameterInfo = (ActionParameterInformation) actionInformation.getParameters().get(i);
      parameters[i] = parameterInfo.resolveParameter(message.getPayload()[i], context);
    }
    return parameters;
  }
}
