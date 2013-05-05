package pl.bristleback.server.bristle.serialization.system;

import pl.bristleback.server.bristle.serialization.system.json.extractor.ValueSerializer;
import pl.bristleback.server.bristle.utils.PropertyAccess;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Objects of this class contains meta information about single serialization operation definition.
 * Serialization operation may come from server/client action resolvers or just from
 * {@link pl.bristleback.server.bristle.message.ConditionObjectSender}.
 * Used by system serialization engines,
 * like {@link pl.bristleback.server.bristle.serialization.system.json.JsonSerializationEngine}.
 * <p/>
 * Created on: 2011-09-04 16:36:29 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PropertySerialization {

  public static final String CONTAINER_ELEMENT_PROPERTY_NAME = "element";

  private final Map<Class, PropertySerialization> implementationSerializations = new HashMap<Class, PropertySerialization>();

  private PropertySerializationConstraints constraints = new PropertySerializationConstraints();

  private PropertyType propertyType;

  private Class propertyClass;

  private Type genericType;

  private List<ClassTypeParameter> typeParameters;

  private ValueSerializer valueSerializer;

  private Map<String, PropertySerialization> propertiesInformation = new HashMap<String, PropertySerialization>();

  private Map<String, PropertyAccess> readableProperties;

  private Map<String, PropertyAccess> writableProperties;

  private boolean usesImplementations;

  private SerializationInput serializationInput;

  public boolean isParametrized() {
    return genericType instanceof ParameterizedType;
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

  public PropertyAccess getReadableProperty(String propertyName) {
    return readableProperties.get(propertyName);
  }

  public PropertyAccess getWritableProperty(String propertyName) {
    return writableProperties.get(propertyName);
  }

  public Map<String, PropertyAccess> getReadableProperties() {
    return readableProperties;
  }

  public void setReadableProperties(Map<String, PropertyAccess> readableProperties) {
    this.readableProperties = readableProperties;
  }

  public Map<String, PropertyAccess> getWritableProperties() {
    return writableProperties;
  }

  public void setWritableProperties(Map<String, PropertyAccess> writableProperties) {
    this.writableProperties = writableProperties;
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

  public boolean isUsingImplementations() {
    return usesImplementations;
  }

  public Map<Class, PropertySerialization> getImplementationSerializations() {
    return implementationSerializations;
  }

  public void setUsingImplementations() {
    this.usesImplementations = true;
  }

  public SerializationInput getSerializationInput() {
    return serializationInput;
  }

  public void setSerializationInput(SerializationInput serializationInput) {
    this.serializationInput = serializationInput;
  }

  public void addImplementationSerialization(Class<?> implementationClass, PropertySerialization implementationSerialization) {
    synchronized (implementationSerializations) {
      implementationSerializations.put(implementationClass, implementationSerialization);
    }
  }
}