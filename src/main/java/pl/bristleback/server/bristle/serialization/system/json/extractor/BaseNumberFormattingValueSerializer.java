package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.json.JSONObject;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.PropertySerializationConstraints;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Base class for built in number formatting serializers, containing utility methods for serializers internal usage.
 * <p/>
 * Created on: 22.12.12 13:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class BaseNumberFormattingValueSerializer<T extends Number> implements FormattingValueSerializer<T> {

  protected abstract NumberFormat createNumberFormatObject(String formatAsString);

  protected abstract T parseFromNotFormattedText(String valueAsString, PropertySerialization information);

  @Override
  public Object prepareFormatHolder(final String formatAsString) {
    return new ThreadLocal<NumberFormat>() {
      @Override
      protected NumberFormat initialValue() {
        return createNumberFormatObject(formatAsString);
      }
    };
  }

  @Override
  public T toValue(String valueAsString, PropertySerialization information) throws Exception {
    if (information.getConstraints().isFormatted()) {
      return parseFromFormattedString(valueAsString, information);
    }
    return parseFromNotFormattedText(valueAsString, information);
  }

  @SuppressWarnings("unchecked")
  protected T parseFromFormattedString(String valueAsString, PropertySerialization information) throws ParseException {
    return (T) getFormat(information.getConstraints()).parse(valueAsString);
  }

  @Override
  public String toText(T value, PropertySerialization information) {
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


