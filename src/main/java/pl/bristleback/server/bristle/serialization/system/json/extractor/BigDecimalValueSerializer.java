package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.utils.StringUtils;

import java.math.BigDecimal;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:13:29 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class BigDecimalValueSerializer implements ValueSerializer<BigDecimal> {
  private static Logger log = Logger.getLogger(BigDecimalValueSerializer.class.getName());

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public BigDecimal toValue(String valueAsString, PropertySerialization information) throws Exception {
    String processedValue = valueAsString.replace(StringUtils.COMMA, StringUtils.DOT);
    return new BigDecimal(processedValue);
  }

  @Override
  public String toText(BigDecimal value, PropertySerialization information) {
    return value.toString();
  }
}
