package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.utils.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:13:29 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class BigDecimalValueSerializer extends BaseNumberFormattingValueSerializer<BigDecimal> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  protected BigDecimal parseFromNotFormattedText(String valueAsString, PropertySerialization information) {
    String processedValue = valueAsString.replace(StringUtils.COMMA, StringUtils.DOT);
    return new BigDecimal(processedValue);
  }

  protected NumberFormat createNumberFormatObject(String formatAsString) {
    DecimalFormat format = new DecimalFormat(formatAsString);
    format.setParseBigDecimal(true);
    return format;
  }
}
