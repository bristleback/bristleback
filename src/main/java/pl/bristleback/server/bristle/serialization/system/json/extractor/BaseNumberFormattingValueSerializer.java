package pl.bristleback.server.bristle.serialization.system.json.extractor;

import pl.bristleback.server.bristle.serialization.system.PropertySerializationConstraints;

import java.text.NumberFormat;

/**
 * Base class for built in number formatting serializers, containing utility methods for serializers internal usage.
 * <p/>
 * Created on: 22.12.12 13:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class BaseNumberFormattingValueSerializer<T extends Number> implements FormattingValueSerializer<T> {

  @SuppressWarnings("unchecked")
  protected NumberFormat getFormat(PropertySerializationConstraints constraints) {
    return ((ThreadLocal<NumberFormat>) constraints.getFormatHolder()).get();
  }
}


