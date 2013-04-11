package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:16:10 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class CharacterValueSerializer extends BaseRawValueSerializer<Character> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  protected Character toValueFromString(String valueAsString, PropertySerialization information) throws Exception {
    return valueAsString.charAt(0);
  }

  @Override
  public String toText(Character value, PropertySerialization information) {
    return value + "";
  }
}
