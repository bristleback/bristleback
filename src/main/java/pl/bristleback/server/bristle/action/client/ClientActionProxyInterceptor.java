package pl.bristleback.server.bristle.action.client;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-26 12:07:38 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionProxyInterceptor implements MethodInterceptor {

  private ClientActionClasses actionClasses;

  private ConditionObjectSender objectSender;

  public void init(ClientActionClasses clientActionClasses, ConditionObjectSender objectsSender) {
    this.actionClasses = clientActionClasses;
    this.objectSender = objectsSender;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object invoke(MethodInvocation invocation) throws Throwable {
    ClientActionInformation actionInformation = resolveActionInformation(invocation);

    Object methodOutput = invocation.proceed();
    Object[] parameters = invocation.getArguments();

    Object payload = null;
    int parametersToSerializeCount = getNumberOfParametersToSerialize(actionInformation);
    if (parametersToSerializeCount == 0) {
      payload = null;
    } else if (parametersToSerializeCount == 1) {
      payload = resolveSinglePayload(actionInformation, parameters, payload);
    } else {
      payload = resolveMapPayload(actionInformation, parameters);
    }

    BristleMessage<Object> message = new BristleMessage<Object>()
      .withName(actionInformation.getFullName()).withPayload(payload);

    ClientActionSender clientActionSender = actionInformation.getResponse();
    clientActionSender.sendClientAction(methodOutput, message, objectSender, actionInformation);

    return methodOutput;
  }

  private Map<String, Object> resolveMapPayload(ClientActionInformation actionInformation, Object[] parameters) {
    Map<String, Object> parametersAsMap = new HashMap<String, Object>();
    int index = 0;
    for (int i = 0; i < parameters.length; i++) {
      Object parameter = parameters[i];
      ClientActionParameterInformation parameterInformation = actionInformation.getParameters().get(i);
      if (parameterInformation.isForSerialization()) {
        parametersAsMap.put("p" + index, parameter);
        index++;
      }
    }
    return parametersAsMap;
  }

  private Object resolveSinglePayload(ClientActionInformation actionInformation, Object[] parameters, Object payload) {
    for (int i = 0; i < parameters.length; i++) {
      ClientActionParameterInformation parameterInformation = actionInformation.getParameters().get(i);
      if (parameterInformation.isForSerialization()) {
        payload = parameters[i];
      }
    }
    return payload;
  }

  private int getNumberOfParametersToSerialize(ClientActionInformation actionInformation) {
    int parametersToSerializeCount = 0;
    for (ClientActionParameterInformation parameterInformation : actionInformation.getParameters()) {
      if (parameterInformation.isForSerialization()) {
        parametersToSerializeCount++;
      }
    }
    return parametersToSerializeCount;
  }

  private ClientActionInformation resolveActionInformation(MethodInvocation invocation) {
    ClientActionClassInformation actionClassInformation = actionClasses.getClientActionClass(invocation.getThis().getClass());
    return actionClassInformation.getClientAction(invocation.getMethod());
  }
}
