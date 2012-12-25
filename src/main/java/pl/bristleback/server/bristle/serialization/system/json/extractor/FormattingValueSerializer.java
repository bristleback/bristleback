package pl.bristleback.server.bristle.serialization.system.json.extractor;

/**
 * Extended {@link ValueSerializer} interface, implementations should be able to handle <code>format</code> property
 * of the {@link pl.bristleback.server.bristle.serialization.system.annotation.Bind},
 * {@link pl.bristleback.server.bristle.serialization.system.annotation.Serialize} and
 * {@link pl.bristleback.server.bristle.serialization.system.annotation.Property} annotations.
 * Because it may be possible to have multiple threads using the same formatting objects,
 * returned format representation must be thread safe.
 * <p/>
 * Created on: 16.12.12 14:40 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface FormattingValueSerializer<T> extends ValueSerializer<T> {

  /**
   * Creates any kind of object that will store information about format used in given field.
   * Returned format object MUST BE capable of performing formatting operations in multiple threads simultaneously.
   *
   * @param formatAsString format passed in serialization annotations.
   * @return format representation or any object providing information about current format.
   */
  Object prepareFormatHolder(String formatAsString);
}
