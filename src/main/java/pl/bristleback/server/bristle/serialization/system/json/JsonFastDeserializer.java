package pl.bristleback.server.bristle.serialization.system.json;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.serialization.PropertyType;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.SerializationException;
import pl.bristleback.server.bristle.utils.PropertyAccess;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-15 14:56:30 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("jsonSerializer.fastDeserializer")
public class JsonFastDeserializer {
  private static Logger log = Logger.getLogger(JsonFastDeserializer.class.getName());

  private Map<PropertyType, TypeDeserializer> deserializationMap;

  public JsonFastDeserializer() {
    deserializationMap = new HashMap<PropertyType, TypeDeserializer>();
    deserializationMap.put(PropertyType.COLLECTION, new CollectionSerializer());
    deserializationMap.put(PropertyType.BEAN, new BeanDeserializer());
    deserializationMap.put(PropertyType.ARRAY, new ArraySerializer());
    deserializationMap.put(PropertyType.MAP, new MapSerializer());
    deserializationMap.put(PropertyType.SIMPLE, new SimpleSerializer());
  }

  public Object deserialize(String serializedObject, PropertySerialization information) throws Exception {
    if (StringUtils.isEmpty(serializedObject)) {
      handleNullValue(information);
      return null;
    }
    TypeDeserializer rootDeserializer = deserializationMap.get(information.getPropertyType());
    Object input = rootDeserializer.resolveInput(serializedObject);
    return rootDeserializer.deserializeElement(input, information);
  }

  private Object doDeserialize(Object input, PropertySerialization information) throws Exception {
    TypeDeserializer rootDeserializer = deserializationMap.get(information.getPropertyType());
    return rootDeserializer.deserializeElement(input, information);
  }

  private void handleNullValue(PropertySerialization propertyInformation) {
    if (propertyInformation.getConstraints().isRequired()) {
      throw new SerializationException(SerializationException.Reason.NOT_NULL_VIOLATION, propertyInformation);
    }
  }

  interface TypeDeserializer<T> {
    Object deserializeElement(T input, PropertySerialization information) throws Exception;

    Object resolveInput(String text) throws Exception;
  }

  private class BeanDeserializer implements TypeDeserializer<JSONObject> {
    @Override
    public Object deserializeElement(JSONObject input, PropertySerialization information) throws Exception {
      Object value = createObject(information.getPropertyClass());

      for (PropertyAccess property : information.getChildrenProperties()) {
        String propertyName = property.getFieldName();
        PropertySerialization childInformation = information.getPropertySerialization(propertyName);
        Object jsonValue = input.opt(propertyName);
        if (jsonValue == null) {
          handleNullValue(childInformation);
        } else {
          Object childValue = doDeserialize(jsonValue, childInformation);
          property.getPropertySetter().invokeWithoutCheck(value, childValue);
        }
      }
      return value;
    }

    @Override
    public Object resolveInput(String text) throws Exception {
      return new JSONObject(text);
    }

    private Object createObject(Class beanClass) throws Exception {
      return beanClass.newInstance();
    }

    @Override
    public String toString() {
      return "Bean serializer";
    }
  }

  private class CollectionSerializer implements TypeDeserializer<JSONArray> {

    @Override
    public Object deserializeElement(JSONArray input, PropertySerialization information) throws Exception {
      List list = new ArrayList();
      PropertySerialization elementInformation = information.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      for (int i = 0; i < input.length(); i++) {
        Object element = doDeserialize(input.get(i), elementInformation);
        list.add(element);
      }

      return list;
    }

    @Override
    public JSONArray resolveInput(String text) throws Exception {
      return new JSONArray(text);
    }

    @Override
    public String toString() {
      return "Collection serializer";
    }
  }

  private class MapSerializer implements TypeDeserializer<JSONObject> {

    @Override
    public Object deserializeElement(JSONObject input, PropertySerialization information) throws Exception {
      Map<String, Object> map = new HashMap<String, Object>();
      PropertySerialization elementInformation = information.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      Iterator keys = input.keys();
      String key;
      Object value;
      while (keys.hasNext()) {
        key = (String) keys.next();
        value = input.get(key);
        if (value == null) {
          continue;
        }
        Object element = doDeserialize(value, elementInformation);
        map.put(key, element);
      }

      return map;
    }

    @Override
    public JSONObject resolveInput(String text) throws Exception {
      return new JSONObject(text);
    }

    @Override
    public String toString() {
      return "Map serializer";
    }
  }

  private class ArraySerializer implements TypeDeserializer<JSONArray> {

    @Override
    public Object deserializeElement(JSONArray input, PropertySerialization information) throws Exception {
      PropertySerialization elementInformation = information.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      boolean primitiveType = ReflectionUtils.isRawClass(elementInformation.getPropertyClass());
      Object array = Array.newInstance(elementInformation.getPropertyClass(), input.length());
      if (primitiveType) {
        processPrimitiveArray(input, array, elementInformation);
      } else {
        processObjectArray(input, (Object[]) array, elementInformation);
      }

      return array;
    }

    private void processObjectArray(JSONArray input, Object[] objects, PropertySerialization elementInformation) throws Exception {
      for (int i = 0; i < objects.length; i++) {
        objects[i] = doDeserialize(input.get(i), elementInformation);
      }
    }

    private void processPrimitiveArray(JSONArray input, Object array, PropertySerialization elementInformation) throws Exception {
      int length = input.length();
      for (int i = 0; i < length; i++) {
        Object elementValue = doDeserialize(input.get(i), elementInformation);
        Array.set(array, i, elementValue);
      }
    }

    @Override
    public JSONArray resolveInput(String text) throws Exception {
      return new JSONArray(text);
    }

    @Override
    public String toString() {
      return "Array serializer";
    }
  }

  private class SimpleSerializer implements TypeDeserializer<Object> {
    @Override
    public Object deserializeElement(Object input, PropertySerialization information) throws Exception {
      return information.getValueSerializer().toValue(input.toString(), information);
    }

    @Override
    public Object resolveInput(String text) throws Exception {
      return text;
    }

    @Override
    public String toString() {
      return "Simple field serializer";
    }
  }
}
