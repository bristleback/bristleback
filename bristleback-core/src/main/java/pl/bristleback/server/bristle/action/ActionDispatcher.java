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
 * A main point for handling incoming messages in system action controller.
 * Message that comes from action controller is partially deserialized,
 * in form of full action name and serialized action parameters.
 * Whole process of action execution is divided into few stages,
 * each stage can be intercepted by one or more {@link pl.bristleback.server.bristle.api.action.ActionInterceptor}.
 * Firstly, action class name and action name are extracted and {@link ActionInformation} object is being resolved.
 * Next, action parameters are deserialized, with usage of {@link pl.bristleback.server.bristle.api.SerializationEngine}.
 * Then action method is invoked with action parameters passed. If exception is thrown by action or by any other component
 * user in action execution process, control is passed back to {@link ActionController}.
 * In the end of process, action response (the object returned by the action method) is sent using {@link ResponseHelper} component.
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
    Object[] actionParameters = resolveActionParameters(action, context);
    context.setActionParameters(actionParameters);
    interceptorPolicyExecutor.executeInterceptorPolicy(action, context);
    return actionParameters;
  }

  @SuppressWarnings("unchecked")
  private Object executeAction(ActionExecutionContext context, ActionInformation action, Object[] parameters) throws Exception {
    context.setStage(ActionExecutionStage.ACTION_EXECUTION);
    Object actionClassInstance = actionsContainer.getActionClassInstance(action.getActionClass(), springIntegration);
    Object response = action.execute(actionClassInstance, parameters);
    context.setResponse(response);
    interceptorPolicyExecutor.executeInterceptorPolicy(action, context);
    return response;
  }

  private void sendResponse(ActionExecutionContext context, ActionInformation action, Object response) throws Exception {
    context.setStage(ActionExecutionStage.RESPONSE_CONSTRUCTION);

    if (!action.getResponseInformation().isVoidResponse()) {
      responseHelper.sendResponse(response, context);
    }
  }

  private Object[] resolveActionParameters(ActionInformation actionInformation, ActionExecutionContext context) throws Exception {
    Object[] parameters = new Object[actionInformation.getParameters().size()];
    BristleMessage<String[]> message = context.getMessage();
    for (int i = 0; i < actionInformation.getParameters().size(); i++) {
      ActionParameterInformation parameterInfo = actionInformation.getParameters().get(i);
      parameters[i] = parameterInfo.resolveParameter(message.getPayload()[i], context);
    }
    return parameters;
  }
}
