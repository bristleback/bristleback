package pl.bristleback.server.bristle.action;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.message.sender.BristleMessage;
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

  public ActionExecutionContext(IdentifiedUser user) {
    stage = ActionExecutionStage.MESSAGE_DESERIALIZATION;
    this.user = user;
  }

  public void extractActionInformation() {
    setStage(ActionExecutionStage.ACTION_EXTRACTION);
    if (message.getName().contains(StringUtils.DOT_AS_STRING)) {
      int dotIndex = message.getName().indexOf('.');
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
}
