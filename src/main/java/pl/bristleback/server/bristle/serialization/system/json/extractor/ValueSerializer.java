package pl.bristleback.server.bristle.serialization.system.json.extractor;

import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-26 13:22:02 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ValueSerializer<T> {

  void init(BristlebackConfig configuration);

  T toValue(String valueAsString, PropertySerialization information) throws Exception;

  String toText(T value, PropertySerialization information) throws Exception;
}
