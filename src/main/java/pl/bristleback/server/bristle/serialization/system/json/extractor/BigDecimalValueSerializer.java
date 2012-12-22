package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.PropertySerializationConstraints;
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
public class BigDecimalValueSerializer implements FormattingValueSerializer<BigDecimal> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public Object prepareFormatHolder(final String formatAsString) {
    return new ThreadLocal<NumberFormat>() {
      @Override
      protected NumberFormat initialValue() {
        DecimalFormat format = new DecimalFormat(formatAsString);
        format.setParseBigDecimal(true);
        return format;
      }
    };
  }

  @Override
  public BigDecimal toValue(String valueAsString, PropertySerialization information) throws Exception {
    if (information.getConstraints().isFormatted()) {
      return (BigDecimal) getFormat(information.getConstraints()).parse(valueAsString);
    }
    String processedValue = valueAsString.replace(StringUtils.COMMA, StringUtils.DOT);
    return new BigDecimal(processedValue);
  }

  @Override
  public String toText(BigDecimal value, PropertySerialization information) {
    if (information.getConstraints().isFormatted()) {
      return JSONObject.quote(getFormat(information.getConstraints()).format(value));
    }
    return value.toString();
  }

  @SuppressWarnings("unchecked")
  private NumberFormat getFormat(PropertySerializationConstraints constraints) {
    return ((ThreadLocal<NumberFormat>) constraints.getFormatHolder()).get();
  }
}
