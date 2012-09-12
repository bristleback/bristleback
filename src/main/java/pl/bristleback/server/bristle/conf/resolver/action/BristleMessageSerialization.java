package pl.bristleback.server.bristle.conf.resolver.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.utils.PropertyUtils;

import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-31 20:13:22 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class BristleMessageSerialization {

  private BristleMessage<String[]> serializedArrayMessage = new BristleMessage<String[]>();

  public Type getSerializedArrayMessageType() {
    return PropertyUtils.getDeclaredField(BristleMessageSerialization.class, "serializedArrayMessage");
  }

}
