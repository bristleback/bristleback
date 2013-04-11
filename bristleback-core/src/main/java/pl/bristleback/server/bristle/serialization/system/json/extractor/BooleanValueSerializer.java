package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-21 13:22:04 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class BooleanValueSerializer extends BaseRawValueSerializer<Boolean> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public Boolean toValueFromString(String valueAsString, PropertySerialization
    information) throws Exception {
    return Boolean.valueOf(valueAsString);
  }

  @Override
  public String toText(Boolean value, PropertySerialization
    information) throws Exception {
    return value.toString();
  }
}