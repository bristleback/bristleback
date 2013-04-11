package pl.bristleback.server.bristle.serialization.system.json.extractor;

import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;

import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-10 22:32:12 <br/>
 *
 * @author Wojciech Niemiec
 */
public class EnumSerializer extends BaseRawValueSerializer<Enum> {

  private Map<String, Enum> enumValues;

  public EnumSerializer(Map<String, Enum> enumValues) {
    this.enumValues = enumValues;
  }

  @Override
  public void init(BristlebackConfig configuration) {

  }

  @Override
  protected Enum toValueFromString(String valueAsString, PropertySerialization information) throws Exception {
    return enumValues.get(valueAsString);
  }

  @Override
  public String toText(Enum value, PropertySerialization information) throws Exception {
    return JsonTokenizer.quote(value.name());
  }
}
