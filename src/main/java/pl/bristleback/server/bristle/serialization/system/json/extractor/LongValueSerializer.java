package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:17:06 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class LongValueSerializer implements ValueSerializer<Long> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public Long toValue(String valueAsString, PropertySerialization information) {
    return Long.valueOf(valueAsString);
  }

  @Override
  public String toText(Long value, PropertySerialization information) {
    return value.toString();
  }
}
