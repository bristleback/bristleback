package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-31 14:22:35 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class IntegerValueSerializer implements ValueSerializer<Integer> {

  @Override
  public void init(BristlebackConfig configuration) {

  }

  public Integer toValue(String value, PropertySerialization information) throws Exception {
    return Integer.parseInt(value);
  }

  @Override
  public String toText(Integer value, PropertySerialization information) {
    return value.toString();
  }
}