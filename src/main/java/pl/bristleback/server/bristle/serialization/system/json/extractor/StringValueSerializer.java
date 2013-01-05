package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-04 18:23:44 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class StringValueSerializer implements ValueSerializer<String> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public String toValue(JsonTokenizer tokenizer, PropertySerialization information) {
    return tokenizer.nextValueAsString();
  }

  @Override
  public String toText(String value, PropertySerialization information) {
    return JsonTokenizer.quote(value);
  }
}