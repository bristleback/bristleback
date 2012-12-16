package pl.bristleback.server.bristle.serialization.system;

import pl.bristleback.server.bristle.serialization.system.json.extractor.ValueSerializer;
import pl.bristleback.server.bristle.utils.PropertyAccess;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-04 16:36:29 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PropertySerialization {

  public static final String CONTAINER_ELEMENT_PROPERTY_NAME = "element";

  private PropertySerializationConstraints constraints = new PropertySerializationConstraints();

  private PropertyType propertyType;
  private Class propertyClass;
  private Type genericType;
  private List<ClassTypeParameter> typeParameters;
  private ValueSerializer valueSerializer;

  private Map<String, PropertySerialization> propertiesInformation = new HashMap<String, PropertySerialization>();
  private List<PropertyAccess> childrenProperties = new ArrayList<PropertyAccess>();

  public static PropertySerialization createSerialization(PropertyType serializationPropertyType, Class propertyClass) {
    PropertySerialization propertySerialization = new PropertySerialization();
    propertySerialization.setPropertyType(serializationPropertyType);
    propertySerialization.setPropertyClass(propertyClass);
    return propertySerialization;
  }

  public boolean isParametrized() {
    return genericType != null && genericType instanceof ParameterizedType;
  }

  @Override
  public String toString() {
    StringBuilder toStringBuilder = new StringBuilder();
    toStringBuilder.append("Property serialization of ");
    if (genericType != null) {
      toStringBuilder.append(genericType);
    } else {
      toStringBuilder.append(propertyClass);
    }
    if (valueSerializer != null) {
      toStringBuilder.append(", value processor class: ").append(valueSerializer.getClass());
    }

    return toStringBuilder.toString();
  }

  public boolean containsPropertySerialization(String propertyPath) {
    return propertiesInformation.containsKey(propertyPath);
  }

  public PropertySerialization getPropertySerialization(String propertyName) {
    return propertiesInformation.get(propertyName);
  }

  public ValueSerializer getValueSerializer() {
    return valueSerializer;
  }

  public void setValueSerializer(ValueSerializer valueSerializer) {
    this.valueSerializer = valueSerializer;
  }

  public Class getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(Class propertyClass) {
    this.propertyClass = propertyClass;
  }

  public void addSerializedProperty(String propertyName, PropertySerialization nestedProperty) {
    propertiesInformation.put(propertyName, nestedProperty);
  }

  public Map<String, PropertySerialization> getPropertiesInformation() {
    return propertiesInformation;
  }

  public Type getGenericType() {
    return genericType;
  }

  public void setGenericType(Type genericType) {
    this.genericType = genericType;
  }

  public PropertyType getPropertyType() {
    return propertyType;
  }

  public void setPropertyType(PropertyType propertyType) {
    this.propertyType = propertyType;
  }

  public List<PropertyAccess> getChildrenProperties() {
    return childrenProperties;
  }

  public void setChildrenProperties(List<PropertyAccess> childrenProperties) {
    this.childrenProperties = childrenProperties;
  }

  public PropertySerializationConstraints getConstraints() {
    return constraints;
  }

  public List<ClassTypeParameter> getTypeParameters() {
    return typeParameters;
  }

  public void setTypeParameters(List<ClassTypeParameter> typeParameters) {
    this.typeParameters = typeParameters;
  }
}