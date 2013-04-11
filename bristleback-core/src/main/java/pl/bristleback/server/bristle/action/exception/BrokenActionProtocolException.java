package pl.bristleback.server.bristle.action.exception;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 09:11:13 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BrokenActionProtocolException extends RuntimeException {

  private ReasonType reasonType;

  public BrokenActionProtocolException(ReasonType reasonType, String message) {
    super(message);
    this.reasonType = reasonType;
  }

  public ReasonType getReasonType() {
    return reasonType;
  }

  public static enum ReasonType {
    INCORRECT_PATH,
    NO_MESSAGE_ID_FOUND,
    NO_ACTION_CLASS_FOUND,
    NO_DEFAULT_ACTION_FOUND,
    NO_ACTION_FOUND
  }
}