package pl.bristleback.server.bristle.action.response;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.authorisation.user.UsersContainer;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.utils.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-05 14:34:55 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ResponseHelper {

  private static final String EXCEPTION_RESPONSE_SIGN = "exc";

  @Inject
  @Named(SpringConfigurationResolver.CONFIG_BEAN_NAME)
  private BristlebackConfig configuration;

  @Inject
  private UsersContainer connectedUsers;

  @ObjectSender
  private ConditionObjectSender conditionObjectSender;

  @PostConstruct
  private void init() {
    conditionObjectSender = new ConditionObjectSender();
    conditionObjectSender.init(configuration, connectedUsers);
  }

  public void sendResponse(Object response, Object serialization, ActionExecutionContext context) throws Exception {
    BristleMessage<Object> responseMessage = prepareMessage(response, context);
    conditionObjectSender.sendMessage(responseMessage, serialization, Collections.singletonList(context.getUser()));
  }

  public void sendExceptionResponse(Object exceptionResponse, ActionExecutionContext context) throws Exception {
    if (exceptionResponse != null) {
      BristleMessage<Object> responseMessage = prepareMessage(exceptionResponse, context);
      conditionObjectSender.sendMessage(responseMessage, Collections.singletonList(context.getUser()));
    }
  }

  private BristleMessage<Object> prepareMessage(Object response, ActionExecutionContext context) {
    BristleMessage<Object> responseMessage = new BristleMessage<Object>();
    String messageName = createMessageName(response, context);
    responseMessage
      .withId(context.getMessage().getId())
      .withName(messageName)
      .withPayload(response);
    return responseMessage;
  }

  private String createMessageName(Object response, ActionExecutionContext context) {
    if (response instanceof ExceptionResponse) {
      return context.getMessage().getName() + StringUtils.COLON + EXCEPTION_RESPONSE_SIGN;
    }
    return context.getMessage().getName();
  }

}

