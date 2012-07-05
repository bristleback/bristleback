package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

import java.math.BigInteger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:14:38 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class BigIntegerValueSerializer implements ValueSerializer<BigInteger> {

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public BigInteger toValue(String valueAsString, PropertySerialization information) {
    return new BigInteger(valueAsString);
  }

  @Override
  public String toText(BigInteger value, PropertySerialization information) {
    return value.toString();
  }
}
