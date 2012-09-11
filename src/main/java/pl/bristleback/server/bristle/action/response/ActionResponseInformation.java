package pl.bristleback.server.bristle.action.response;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-02 15:42:24 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionResponseInformation {

  private boolean voidResponse;
  private Object serialization;

  public boolean isVoidResponse() {
    return voidResponse;
  }

  public void setVoidResponse(boolean voidResponse) {
    this.voidResponse = voidResponse;
  }

  public Object getSerialization() {
    return serialization;
  }

  public void setSerialization(Object serialization) {
    this.serialization = serialization;
  }
}
