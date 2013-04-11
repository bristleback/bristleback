package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:17:06 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class LongValueSerializer extends BaseNumberFormattingValueSerializer<Long> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  protected NumberFormat createNumberFormatObject(String formatAsString) {
    DecimalFormat format = new DecimalFormat(formatAsString);
    format.setParseBigDecimal(false);
    format.setParseIntegerOnly(true);
    return format;
  }

  @Override
  protected Long parseFromNotFormattedText(String valueAsString, PropertySerialization information) {
    return Long.valueOf(valueAsString);
  }
}
