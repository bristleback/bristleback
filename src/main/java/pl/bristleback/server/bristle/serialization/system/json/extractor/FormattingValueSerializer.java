package pl.bristleback.server.bristle.serialization.system.json.extractor;

import java.text.Format;

/**
 *
 * <p/>
 * Created on: 16.12.12 14:40 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface FormattingValueSerializer<T> extends ValueSerializer<T> {

  Format prepareFormat(String formatAsString);
}
