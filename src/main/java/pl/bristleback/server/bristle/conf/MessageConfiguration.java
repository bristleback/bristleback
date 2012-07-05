package pl.bristleback.server.bristle.conf;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.MessageDispatcher;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-28 14:38:40 <br/>
 *
 * @author Wojciech Niemiec
 */
public class MessageConfiguration {
  private static Logger log = Logger.getLogger(MessageConfiguration.class.getName());

  private MessageDispatcher messageDispatcher;

  public MessageDispatcher getMessageDispatcher() {
    return messageDispatcher;
  }

  public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
    this.messageDispatcher = messageDispatcher;
  }
}
