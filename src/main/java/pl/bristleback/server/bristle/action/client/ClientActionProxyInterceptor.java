package pl.bristleback.server.bristle.action.client;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
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
  private static Logger log = Logger.getLogger(ClientActionProxyInterceptor.class.getName());

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
    if (parameters.length == 1) {
      payload = parameters[0];
    } else if (parameters.length > 1) {
      int parametersToSerializeCount = 0;
      parametersToSerializeCount = getNumberOfParametersToSerialize(actionInformation, parametersToSerializeCount);
      if (parametersToSerializeCount == 1) {
        payload = resolveSinglePayload(actionInformation, parameters, payload);
      } else {
        payload = resolveMapPayload(actionInformation, parameters);
      }
    }

    BristleMessage<Object> message = new BristleMessage<Object>()
      .withName(actionInformation.getFullName()).withPayload(payload);

    ClientActionSender clientActionSender = actionInformation.getResponse();
    clientActionSender.sendClientAction(methodOutput, message, objectSender);

    return methodOutput;
  }

  private Object resolveMapPayload(ClientActionInformation actionInformation, Object[] parameters) {
    Object payload;
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
    payload = parametersAsMap;
    return payload;
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

  private int getNumberOfParametersToSerialize(ClientActionInformation actionInformation, int parametersToSerializeCount) {
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
