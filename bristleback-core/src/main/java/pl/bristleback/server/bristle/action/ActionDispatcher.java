/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorsExecutor;
import pl.bristleback.server.bristle.action.response.ResponseHelper;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.conf.BristlebackComponentsContainer;
import pl.bristleback.server.bristle.conf.resolver.action.ActionClassesResolver;
import pl.bristleback.common.serialization.message.BristleMessage;

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
  private BristlebackComponentsContainer componentsContainer;

  @Inject
  private ActionInterceptorsExecutor interceptorPolicyExecutor;

  public void init(BristlebackConfig configuration) {
    actionsContainer = actionClassesResolver.resolve();
    responseHelper.init(configuration);
  }

  public void dispatch(ActionExecutionContext context) throws Exception {
    extractAction(context);
    Object[] parameters = extractActionParameters(context);
    Object response = executeAction(context, parameters);
    sendResponse(context, response);
  }

  private ActionInformation extractAction(ActionExecutionContext context) {
    context.extractActionInformation();
    ActionClassInformation actionClass = actionsContainer.getActionClass(context.getActionClassName());
    ActionInformation action = actionClass.getActionToExecute(context);
    context.setAction(action);

    context.setActionClassInstance(actionsContainer.getActionClassInstance(action.getActionClass(), componentsContainer));
    interceptorPolicyExecutor.executeInterceptorPolicy(context);

    return action;
  }

  private Object[] extractActionParameters(ActionExecutionContext context) throws Exception {
    context.setStage(ActionExecutionStage.PARAMETERS_EXTRACTION);
    Object[] actionParameters = resolveActionParameters(context);
    context.setActionParameters(actionParameters);
    interceptorPolicyExecutor.executeInterceptorPolicy(context);
    return actionParameters;
  }

  @SuppressWarnings("unchecked")
  private Object executeAction(ActionExecutionContext context, Object[] parameters) throws Exception {
    context.setStage(ActionExecutionStage.ACTION_EXECUTION);

    Object response = context.getAction().execute(context.getActionClassInstance(), parameters);
    context.setResponse(response);
    interceptorPolicyExecutor.executeInterceptorPolicy(context);
    return response;
  }

  private void sendResponse(ActionExecutionContext context, Object response) throws Exception {
    context.setStage(ActionExecutionStage.RESPONSE_CONSTRUCTION);

    if (!context.getAction().getResponseInformation().isVoidResponse()) {
      responseHelper.sendResponse(response, context);
    }
  }

  private Object[] resolveActionParameters(ActionExecutionContext context) throws Exception {
    ActionInformation actionInformation = context.getAction();
    Object[] parameters = new Object[actionInformation.getParameters().size()];
    BristleMessage<String[]> message = context.getMessage();
    for (int i = 0; i < actionInformation.getParameters().size(); i++) {
      ActionParameterInformation parameterInfo = actionInformation.getParameters().get(i);
      parameters[i] = parameterInfo.resolveParameter(message.getPayload()[i], context);
    }
    return parameters;
  }
}
