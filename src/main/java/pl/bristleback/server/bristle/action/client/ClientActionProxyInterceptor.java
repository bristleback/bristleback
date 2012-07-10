package pl.bristleback.server.bristle.action.client;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
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
    Map<String, Object> parametersAsMap = new HashMap<String, Object>();
    int index = 0;
    for (int i = 0; i < parameters.length; i++) {
      Object parameter = parameters[i];
      ActionParameterInformation parameterInformation = actionInformation.getParameters().get(i);
      if (parameterInformation.getExtractor().isDeserializationRequired()) {
        parametersAsMap.put("p" + index, parameter);
        index++;
      }
    }

    BristleMessage<Map<String, Object>> message = new BristleMessage<Map<String, Object>>()
      .withName(actionInformation.getFullName()).withPayload(parametersAsMap);

    ClientActionSender clientActionSender = actionInformation.getResponse();
    clientActionSender.sendClientAction(methodOutput, message, objectSender);

    return methodOutput;
  }

  private ClientActionInformation resolveActionInformation(MethodInvocation invocation) {
    ClientActionClassInformation actionClassInformation = actionClasses.getClientActionClass(invocation.getThis().getClass());
    return actionClassInformation.getClientAction(invocation.getMethod());
  }
}
