package pl.bristleback.server.bristle.serialization.system.json.converter;

/**
 * This enumeration gathers types of json object fragment.
 * <p/>
 * Created on: 02.01.13 19:59 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum JsonTokenType {

  OBJECT_START,
  OBJECT_END,
  ARRAY_START,
  ARRAY_END,
  PROPERTY_NAME,
  PROPERTY_NAME_OR_RAW_VALUE,
  PROPERTY_VALUE,

  END_OF_JSON
}
