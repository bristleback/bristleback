package pl.bristleback.server.bristle.action;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.exceptions.BrokenActionProtocolException;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.utils.StringUtils;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-04 14:47:51 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionExecutionContext {

  private ActionExecutionStage stage;

  private BristleMessage<String[]> message;

  private IdentifiedUser user;

  private String actionClassName;

  private String actionName;

  private ActionInformation action;

  public ActionExecutionContext(IdentifiedUser user) {
    stage = ActionExecutionStage.MESSAGE_DESERIALIZATION;
    this.user = user;
  }

  public void extractActionInformation() {
    if (org.apache.commons.lang.StringUtils.isEmpty(message.getId())) {
      throw new BrokenActionProtocolException(BrokenActionProtocolException.ReasonType.NO_MESSAGE_ID_FOUND,
        "Request Id must not be null.");
    }
    setStage(ActionExecutionStage.ACTION_EXTRACTION);
    if (message.getName().contains(StringUtils.DOT_AS_STRING)) {
      int dotIndex = message.getName().indexOf(StringUtils.DOT);
      actionClassName = message.getName().substring(0, dotIndex);
      actionName = message.getName().substring(dotIndex + 1);
    } else {
      actionClassName = message.getName();
      actionName = StringUtils.EMPTY;
    }
  }

  public String getActionClassName() {
    return actionClassName;
  }

  public String getActionName() {
    return actionName;
  }

  public ActionExecutionStage getStage() {
    return stage;
  }

  public void setStage(ActionExecutionStage stage) {
    this.stage = stage;
  }

  public BristleMessage<String[]> getMessage() {
    return message;
  }

  public void setMessage(BristleMessage<String[]> message) {
    this.message = message;
  }

  public IdentifiedUser getUser() {
    return user;
  }

  public ActionInformation getAction() {
    return action;
  }

  public void setAction(ActionInformation action) {
    this.action = action;
  }
}
