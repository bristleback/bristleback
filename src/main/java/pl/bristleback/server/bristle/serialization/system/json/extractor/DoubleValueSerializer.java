package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.utils.StringUtils;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:15:07 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class DoubleValueSerializer implements ValueSerializer<Double> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public Double toValue(String valueAsString, PropertySerialization information) throws Exception {
    String processedValue = valueAsString.replace(StringUtils.COMMA, StringUtils.DOT);
    return Double.parseDouble(processedValue);
  }

  @Override
  public String toText(Double value, PropertySerialization information) {
    return value.toString();
  }
}
