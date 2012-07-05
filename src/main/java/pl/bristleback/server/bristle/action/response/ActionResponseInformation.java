package pl.bristleback.server.bristle.action.response;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-02 15:42:24 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionResponseInformation {
  private static Logger log = Logger.getLogger(ActionResponseInformation.class.getName());

  private Object serialization;

  public Object getSerialization() {
    return serialization;
  }

  public void setSerialization(Object serialization) {
    this.serialization = serialization;
  }
}
