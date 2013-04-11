package pl.bristleback.server.bristle.serialization.system.json;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.PropertyType;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;
import pl.bristleback.server.bristle.utils.Getter;
import pl.bristleback.server.bristle.utils.PropertyAccess;
import pl.bristleback.server.bristle.utils.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-08 16:04:50 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("jsonSerializer.fastSerializer")
public class JsonFastSerializer {

  private static final String EMPTY_JSON_ARRAY = StringUtils.LEFT_BRACKET + "" + StringUtils.RIGHT_BRACKET;
  private static final String EMPTY_JSON_OBJECT = StringUtils.LEFT_CURLY + "" + StringUtils.RIGHT_CURLY;
  private static final String NULL_OBJECT = "null";
  private Map<PropertyType, TypeSerializer> serializationMap;

  public JsonFastSerializer() {
    serializationMap = new HashMap<PropertyType, TypeSerializer>();
    serializationMap.put(PropertyType.COLLECTION, new CollectionSerializer());
    serializationMap.put(PropertyType.BEAN, new BeanSerializer());
    serializationMap.put(PropertyType.ARRAY, new ArraySerializer());
    serializationMap.put(PropertyType.MAP, new MapSerializer());
    serializationMap.put(PropertyType.SIMPLE, new SimpleSerializer());
  }

  @SuppressWarnings("unchecked")
  public String serializeObject(Object value, PropertySerialization information) throws Exception {
    if (value == null) {
      return NULL_OBJECT;
    }
    return serializationMap.get(information.getPropertyType()).serialize(value, information);
  }

  interface TypeSerializer {
    String serialize(Object value, PropertySerialization information) throws Exception;
  }

  private class CollectionSerializer implements TypeSerializer {
    @Override
    public String serialize(Object value, PropertySerialization information) throws Exception {
      Collection valueAsCollection = (Collection) value;
      if (valueAsCollection.isEmpty()) {
        return EMPTY_JSON_ARRAY;
      } else {
        StringBuilder jsonArrayBuilder = new StringBuilder();
        jsonArrayBuilder.append(StringUtils.LEFT_BRACKET);
        PropertySerialization elementInformation =
          information.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
        Iterator it = valueAsCollection.iterator();
        while (it.hasNext()) {
          jsonArrayBuilder.append(serializeObject(it.next(), elementInformation));
          if (it.hasNext()) {
            jsonArrayBuilder.append(StringUtils.COMMA);
          }
        }
        jsonArrayBuilder.append(StringUtils.RIGHT_BRACKET);
        return jsonArrayBuilder.toString();
      }
    }
  }

  private class BeanSerializer implements TypeSerializer {
    @Override
    public String serialize(Object value, PropertySerialization information) throws Exception {
      StringBuilder jsonObjectBuilder = new StringBuilder();
      jsonObjectBuilder.append(StringUtils.LEFT_CURLY);
      Iterator<PropertyAccess> it = information.getReadableProperties().values().iterator();
      while (it.hasNext()) {
        Getter childGetter = it.next().getPropertyGetter();
        jsonObjectBuilder.append(JsonTokenizer.quotePropertyName(childGetter.getFieldName())).append(StringUtils.COLON);
        PropertySerialization childInformation = information.getPropertySerialization(childGetter.getFieldName());
        Object childValue = childGetter.invokeWithoutCheck(value);
        jsonObjectBuilder.append(serializeObject(childValue, childInformation));
        if (it.hasNext()) {
          jsonObjectBuilder.append(StringUtils.COMMA);
        }
      }
      jsonObjectBuilder.append(StringUtils.RIGHT_CURLY);
      return jsonObjectBuilder.toString();
    }
  }

  private class ArraySerializer implements TypeSerializer {

    @Override
    public String serialize(Object value, PropertySerialization information) throws Exception {
      if (value.getClass().getComponentType().isPrimitive()) {
        return serializeArrayOfPrimitives(value, information);
      } else {
        return serializeArrayOfObjects(value, information);
      }
    }

    private String serializeArrayOfPrimitives(Object value, PropertySerialization information) throws Exception {
      int length = Array.getLength(value);
      if (length == 0) {
        return EMPTY_JSON_ARRAY;
      }
      StringBuilder jsonArrayBuilder = new StringBuilder();
      jsonArrayBuilder.append(StringUtils.LEFT_BRACKET);
      PropertySerialization elementSerialization = information.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      for (int i = 0; i < length; i++) {
        String elementSerializedValue = serializeObject(Array.get(value, i), elementSerialization);
        jsonArrayBuilder.append(elementSerializedValue);
        if (i < length - 1) {
          jsonArrayBuilder.append(StringUtils.COMMA);
        }
      }
      jsonArrayBuilder.append(StringUtils.RIGHT_BRACKET);
      return jsonArrayBuilder.toString();
    }

    private String serializeArrayOfObjects(Object value, PropertySerialization information) throws Exception {
      Object[] valueAsObjectArray = (Object[]) value;
      if (valueAsObjectArray.length == 0) {
        return EMPTY_JSON_ARRAY;
      }
      StringBuilder jsonArrayBuilder = new StringBuilder();
      jsonArrayBuilder.append(StringUtils.LEFT_BRACKET);
      PropertySerialization elementSerialization = information.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      for (int i = 0; i < valueAsObjectArray.length; i++) {
        String elementSerializedValue = serializeObject(valueAsObjectArray[i], elementSerialization);
        jsonArrayBuilder.append(elementSerializedValue);
        if (i < valueAsObjectArray.length - 1) {
          jsonArrayBuilder.append(StringUtils.COMMA);
        }
      }
      jsonArrayBuilder.append(StringUtils.RIGHT_BRACKET);
      return jsonArrayBuilder.toString();
    }
  }

  private class MapSerializer implements TypeSerializer {
    @Override
    public String serialize(Object value, PropertySerialization information) throws Exception {
      Map<?, ?> valueAsMap = (Map) value;
      if (valueAsMap.isEmpty()) {
        return EMPTY_JSON_OBJECT;
      }
      StringBuilder jsonObjectBuilder = new StringBuilder();
      jsonObjectBuilder.append(StringUtils.LEFT_CURLY);
      Iterator<? extends Map.Entry<?, ?>> it = valueAsMap.entrySet().iterator();
      PropertySerialization defaultElementInformation = information.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      while (it.hasNext()) {
        Map.Entry<?, ?> entry = it.next();
        jsonObjectBuilder.append(JsonTokenizer.quotePropertyName(entry.getKey().toString())).append(StringUtils.COLON);
        PropertySerialization elementSerialization = getMapElementSerialization((String) entry.getKey(), information, defaultElementInformation);
        String serializedEntryValue = serializeObject(entry.getValue(), elementSerialization);
        jsonObjectBuilder.append(serializedEntryValue);
        if (it.hasNext()) {
          jsonObjectBuilder.append(StringUtils.COMMA);
        }
      }
      jsonObjectBuilder.append(StringUtils.RIGHT_CURLY);
      return jsonObjectBuilder.toString();
    }

    private PropertySerialization getMapElementSerialization(String key, PropertySerialization mapInformation, PropertySerialization baseElementInformation) {
      if (mapInformation.getPropertiesInformation().size() == 1) {
        return baseElementInformation;
      }
      if (mapInformation.containsPropertySerialization(key)) {
        return mapInformation.getPropertySerialization(key);
      }
      return baseElementInformation;
    }
  }

  private class SimpleSerializer implements TypeSerializer {
    @Override
    public String serialize(Object value, PropertySerialization information) throws Exception {
      return information.getValueSerializer().toText(value, information);
    }
  }
}