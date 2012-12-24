package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:14:38 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class BigIntegerValueSerializer extends BaseNumberFormattingValueSerializer<BigInteger> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  protected BigInteger parseFromFormattedString(String valueAsString, PropertySerialization information) throws ParseException {
    return ((BigDecimal) getFormat(information.getConstraints()).parse(valueAsString)).toBigInteger();
  }

  @Override
  protected NumberFormat createNumberFormatObject(String formatAsString) {
    DecimalFormat format = new DecimalFormat(formatAsString);
    format.setParseBigDecimal(true);
    format.setParseIntegerOnly(true);
    return format;
  }

  @Override
  protected BigInteger parseFromNotFormattedText(String valueAsString, PropertySerialization information) {
    return new BigInteger(valueAsString);
  }
}
