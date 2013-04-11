package pl.bristleback.server.bristle.serialization.system.json;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.serialization.system.DeserializationException;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.PropertyType;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenType;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;
import pl.bristleback.server.bristle.utils.PropertyAccess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-15 14:56:30 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class JsonFastDeserializer {

  private Map<PropertyType, TypeDeserializer> deserializationMap;

  public JsonFastDeserializer() {
    deserializationMap = new HashMap<PropertyType, TypeDeserializer>();
    deserializationMap.put(PropertyType.MAP, new MapDeserializer());
    deserializationMap.put(PropertyType.COLLECTION, new CollectionDeserializer());
    deserializationMap.put(PropertyType.ARRAY, new ArrayDeserializer());
    deserializationMap.put(PropertyType.SIMPLE, new SimpleDeserializer());
    deserializationMap.put(PropertyType.BEAN, new BeanDeserializer());
  }

  public Object deserialize(String serializedObject, PropertySerialization information) throws Exception {
    if (StringUtils.isEmpty(serializedObject)) {
      handleNullValue(information);
      return null;
    }
    JsonTokenizer tokenizer = new JsonTokenizer(serializedObject);
    return doDeserialize(tokenizer, information);

  }

  private Object doDeserialize(JsonTokenizer tokenizer, PropertySerialization information) throws Exception {
    TypeDeserializer rootDeserializer = deserializationMap.get(information.getPropertyType());
    return rootDeserializer.deserializeElement(tokenizer, information);
  }

  private void handleNullValue(PropertySerialization propertyInformation) {
    if (propertyInformation.getConstraints().isRequired()) {
      throw new DeserializationException("Required value cannot be null");
    }
  }

  interface TypeDeserializer {

    Object deserializeElement(JsonTokenizer tokenizer, PropertySerialization information) throws Exception;

  }

  abstract class BaseMapDeserializer<T> implements TypeDeserializer {

    @Override
    public Object deserializeElement(JsonTokenizer tokenizer, PropertySerialization information) throws Exception {
      T value = createObject(information);

      JsonTokenType tokenType = tokenizer.nextToken();
      if (tokenType != JsonTokenType.OBJECT_START) {
        throw new DeserializationException("Serialized json object must start with '{' character");
      }
      int requiredPropertiesCount = 0;

      while (tokenizer.nextToken() == JsonTokenType.PROPERTY_NAME_OR_RAW_VALUE) {
        String propertyName = tokenizer.getLastTokenValue();
        PropertySerialization childInformation = getElementSerialization(propertyName, information);
        if (childInformation != null) {
          Object child = doDeserialize(tokenizer, childInformation);
          addChild(value, child, information, propertyName);
          if (childInformation.getConstraints().isRequired()) {
            requiredPropertiesCount++;
          }
        } else {
          tokenizer.fastForwardValue();
        }
      }
      if (tokenizer.getLastTokenType() != JsonTokenType.OBJECT_END) {
        throw new DeserializationException("Serialized json object must end with '}' character");
      }
      processRequiredProperties(information, requiredPropertiesCount);
      return value;
    }

    protected abstract void processRequiredProperties(PropertySerialization information, int requiredPropertiesCount);

    protected abstract void addChild(T value, Object child, PropertySerialization parentInformation, String propertyName) throws Exception;

    protected abstract PropertySerialization getElementSerialization(String key, PropertySerialization parent);

    protected abstract T createObject(PropertySerialization information) throws Exception;
  }

  class BeanDeserializer extends BaseMapDeserializer<Object> {

    @Override
    protected Object createObject(PropertySerialization information) throws Exception {
      return information.getPropertyClass().newInstance();
    }

    @Override
    protected void addChild(Object value, Object child, PropertySerialization parentInformation, String propertyName) throws Exception {
      PropertyAccess childAccess = parentInformation.getWritableProperty(propertyName);
      childAccess.getPropertySetter().invokeWithoutCheck(value, child);
    }

    @Override
    protected PropertySerialization getElementSerialization(String key, PropertySerialization parent) {
      return parent.getPropertySerialization(key);
    }

    @Override
    protected void processRequiredProperties(PropertySerialization information, int requiredPropertiesCount) {
      if (information.getConstraints().getRequiredChildren() > requiredPropertiesCount) {
        throw new DeserializationException("Required value cannot be null");
      }
    }
  }

  class MapDeserializer extends BaseMapDeserializer<Map<String, Object>> {

    @Override
    protected Map<String, Object> createObject(PropertySerialization information) throws Exception {
      return new HashMap<String, Object>();
    }

    @Override
    protected void addChild(Map<String, Object> value, Object child, PropertySerialization parentInformation, String propertyName) throws Exception {
      value.put(propertyName, child);
    }

    @Override
    protected void processRequiredProperties(PropertySerialization information, int requiredPropertiesCount) {
    }

    protected PropertySerialization getElementSerialization(String key, PropertySerialization parent) {
      PropertySerialization defaultElementInformation = parent.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      if (parent.getPropertiesInformation().size() == 1) {
        return defaultElementInformation;
      }
      if (parent.containsPropertySerialization(key)) {
        return parent.getPropertySerialization(key);
      }
      return defaultElementInformation;
    }
  }

  class SimpleDeserializer implements TypeDeserializer {

    @Override
    public Object deserializeElement(JsonTokenizer tokenizer, PropertySerialization information) throws Exception {
      return information.getValueSerializer().toValue(tokenizer, information);
    }
  }

  class ArrayDeserializer extends CollectionDeserializer implements TypeDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public Object deserializeElement(JsonTokenizer tokenizer, PropertySerialization information) throws Exception {
      List<Object> arrayAsList = (List<Object>) super.deserializeElement(tokenizer, information);

      PropertySerialization elementInformation = information.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      Object array = Array.newInstance(elementInformation.getPropertyClass(), arrayAsList.size());
      boolean primitiveType = elementInformation.getPropertyClass().isPrimitive();
      if (primitiveType) {
        addElementsToPrimitiveArray(array, arrayAsList);
      } else {
        addElementsToObjectArray((Object[]) array, arrayAsList);
      }
      return array;
    }

    private void addElementsToObjectArray(Object[] array, List<Object> arrayAsList) {
      arrayAsList.toArray(array);
    }

    private void addElementsToPrimitiveArray(Object array, List<Object> arrayAsList) {
      int length = arrayAsList.size();
      for (int i = 0; i < length; i++) {
        Array.set(array, i, arrayAsList.get(i));
      }
    }
  }

  class CollectionDeserializer implements TypeDeserializer {

    @Override
    public Object deserializeElement(JsonTokenizer tokenizer, PropertySerialization information) throws Exception {
      List<Object> list = new ArrayList<Object>();

      PropertySerialization elementInformation = information.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      JsonTokenType tokenType = tokenizer.nextToken();
      if (tokenType != JsonTokenType.ARRAY_START) {
        throw new DeserializationException("Serialized json array must start with '[' character");
      }

      while (tokenizer.nextToken() != JsonTokenType.ARRAY_END) {
        if (tokenizer.getLastTokenType() == JsonTokenType.END_OF_JSON) {
          throw new DeserializationException("Serialized json array must end with ']' character");
        }
        tokenizer.setNextReadRepeatedFromLast();
        Object child = doDeserialize(tokenizer, elementInformation);
        list.add(child);
      }

      return list;
    }
  }
}
