package pl.bristleback.server.bristle.serialization.system;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.conf.resolver.action.BristleMessageSerializationUtils;
import pl.bristleback.server.bristle.conf.resolver.serialization.SerializationInputResolver;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.SerializationBundle;
import pl.bristleback.server.bristle.serialization.SerializationResolvingException;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;
import pl.bristleback.server.bristle.serialization.system.annotation.SerializeBundle;
import pl.bristleback.server.bristle.serialization.system.json.extractor.EnumSerializer;
import pl.bristleback.server.bristle.serialization.system.json.extractor.FormattingValueSerializer;
import pl.bristleback.server.bristle.serialization.system.json.extractor.ValueProcessorsResolver;
import pl.bristleback.server.bristle.serialization.system.json.extractor.ValueSerializer;
import pl.bristleback.server.bristle.utils.PropertyAccess;
import pl.bristleback.server.bristle.utils.PropertyUtils;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-08 20:23:35 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component("system.serializationResolver")
public class BristleSerializationResolver implements SerializationResolver<PropertySerialization> {

  private static Logger log = Logger.getLogger(BristleSerializationResolver.class.getName());

  @Inject
  private ExtractorsContainer extractorsContainer;

  @Inject
  private ValueProcessorsResolver valueProcessorsResolver;

  @Inject
  private SerializationInputResolver serializationInputResolver;

  @Inject
  private BristleMessageSerializationUtils messageSerializationUtils;

  public void init(BristlebackConfig configuration) {
    valueProcessorsResolver.resolvePropertyValueExtractors(extractorsContainer);
  }

  @Override
  public SerializationBundle initSerializationBundle(Field objectSenderField) {
    SerializationBundle serializationBundle = new SerializationBundle();
    Annotation[] annotations = objectSenderField.getAnnotations();
    Serialize serializeAnnotation = findAnnotation(Serialize.class, annotations);
    if (serializeAnnotation != null) {
      PropertySerialization messageSerialization = createSerializationUsingSerializeAnnotation(serializeAnnotation);
      serializationBundle.addSerialization(serializeAnnotation.target(), messageSerialization);
      return serializationBundle;
    }
    SerializeBundle serializeBundleAnnotation = findAnnotation(SerializeBundle.class, annotations);
    if (serializeBundleAnnotation != null) {
      for (Serialize serializeAnnotationElement : serializeBundleAnnotation.value()) {
        PropertySerialization messageSerialization = createSerializationUsingSerializeAnnotation(serializeAnnotationElement);
        serializationBundle.addSerialization(serializeAnnotationElement.target(), messageSerialization);
      }
      return serializationBundle;
    }
    return serializationBundle;
  }

  private PropertySerialization createSerializationUsingSerializeAnnotation(Serialize serializeAnnotation) {
    PropertySerialization messageSerialization = resolveSerialization(messageSerializationUtils.getSimpleMessageType());

    SerializationInput input = serializationInputResolver.resolveInputInformation(serializeAnnotation);
    PropertySerialization payloadSerialization = resolveSerialization(serializeAnnotation.target(), input);

    setSerializationForField(messageSerialization, BristleMessage.PAYLOAD_PROPERTY_NAME, payloadSerialization);
    return messageSerialization;
  }

  @Override
  public void setSerializationForField(PropertySerialization parentSerialization, String fieldName, PropertySerialization fieldSerialization) {
    parentSerialization.getPropertiesInformation().put(fieldName, fieldSerialization);
  }

  @Override
  public PropertySerialization resolveSerialization(Type objectType, Annotation... annotations) {
    Bind bindAnnotation = findAnnotation(Bind.class, annotations);
    if (bindAnnotation != null) {
      SerializationInput input = serializationInputResolver.resolveInputInformation(bindAnnotation);
      return resolveSerialization(objectType, input);
    }
    Serialize serializeAnnotation = findAnnotation(Serialize.class, annotations);
    if (serializeAnnotation != null) {
      SerializationInput input = serializationInputResolver.resolveInputInformation(serializeAnnotation);
      return resolveSerialization(objectType, input);
    }

    return resolveSerialization(objectType, new SerializationInput());
  }

  @SuppressWarnings("unchecked")
  private <T> T findAnnotation(Class<T> annotationType, Annotation[] parameterAnnotations) {
    for (Annotation annotation : parameterAnnotations) {
      if (annotation.annotationType().equals(annotationType)) {
        return (T) annotation;
      }
    }
    return null;
  }

  private PropertySerialization resolveSerialization(Type objectType, SerializationInput input) {
    return doResolveSerialization(null, objectType, input);
  }

  private PropertySerialization doResolveSerialization(PropertySerialization parentSerialization, Type objectType, SerializationInput input) {
    if (isDefaultSerializationInContainer(objectType, input)) {
      return extractorsContainer.getDefaultPropertySerialization(objectType);
    }
    if (isPropertySkipped(input)) {
      return null;
    }

    PropertySerialization serialization = resolveBasicInformation(parentSerialization, objectType);
    serialization.setSerializationInput(input);
    if (serialization.isParametrized()) {
      setTypeVariableParameters(parentSerialization, serialization);
    }

    if (!input.containsNonDefaultProperties()) {
      extractorsContainer.addDefaultPropertySerialization(serialization);
    }
    if (extractorsContainer.containsValueProcessor(serialization.getPropertyClass())) {
      createSimpleTypeSerialization(serialization);
    } else {
      createComplexObjectSerialization(parentSerialization, serialization, input);
    }
    addConstraints(serialization, input.getPropertyInformation());

    return serialization;
  }

  private PropertySerialization resolveBasicInformation(PropertySerialization parentSerialization, Type objectType) {
    PropertySerialization serialization = new PropertySerialization();
    Class objectClass;
    ParameterizedType parameterizedType = null;
    if (objectType instanceof ParameterizedType) {
      parameterizedType = (ParameterizedType) objectType;
      objectClass = (Class) parameterizedType.getRawType();
    } else if (objectType instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) objectType).getGenericComponentType();
      PropertySerialization childArrayType = resolveBasicInformation(parentSerialization, componentType);
      objectClass = Array.newInstance(childArrayType.getPropertyClass(), 0).getClass();
    } else if (objectType instanceof TypeVariable) {
      Type realType = findRealTypeFromTypeVariable(parentSerialization, (TypeVariable) objectType);
      return resolveBasicInformation(parentSerialization, realType);
    } else {
      objectClass = (Class) objectType;
    }

    serialization.setPropertyClass(objectClass);
    serialization.setGenericType(parameterizedType);
    return serialization;
  }

  private Type findRealTypeFromTypeVariable(PropertySerialization parentSerialization, TypeVariable genericReturnType) {
    for (ClassTypeParameter classTypeParameter : parentSerialization.getTypeParameters()) {
      if (classTypeParameter.getParameterName().equals(genericReturnType)) {
        return classTypeParameter.getParameterType();
      }
    }
    throw new SerializationResolvingException("Field in class " + parentSerialization.getPropertyClass().getName()
      + " is of type variable " + genericReturnType.getName() + ", but the real type of that field cannot be found.");
  }

  private void setTypeVariableParameters(PropertySerialization parentSerialization, PropertySerialization serialization) {
    TypeVariable<?>[] typeVars = serialization.getPropertyClass().getTypeParameters();
    List<ClassTypeParameter> typeParameters = new ArrayList<ClassTypeParameter>(typeVars.length);
    for (int i = 0; i < typeVars.length; i++) {
      TypeVariable<?> typeVar = typeVars[i];
      Type parameterType = resolveType(parentSerialization, ((ParameterizedType) serialization.getGenericType()).getActualTypeArguments()[i]);
      typeParameters.add(new ClassTypeParameter(typeVar, parameterType));
    }
    serialization.setTypeParameters(typeParameters);
  }

  private void addConstraints(PropertySerialization serialization, PropertyInformation propertyInput) {
    if (propertyInput != null) {
      PropertySerializationConstraints constraints = serialization.getConstraints();
      constraints.setRequired(propertyInput.isRequired());
      if (isValueSerializerAbleToFormatData(propertyInput, serialization)) {
        FormattingValueSerializer formattingValueSerializer = (FormattingValueSerializer) serialization.getValueSerializer();
        constraints.setFormatHolder(formattingValueSerializer.prepareFormatHolder(propertyInput.getFormat()));
      }
    }
  }

  private boolean isValueSerializerAbleToFormatData(PropertyInformation propertyInput, PropertySerialization serialization) {
    return StringUtils.isNotBlank(propertyInput.getFormat()) && serialization.getPropertyType() == PropertyType.SIMPLE && serialization.getValueSerializer() instanceof FormattingValueSerializer;
  }

  private PropertySerialization createSimpleTypeSerialization(PropertySerialization serialization) {
    ValueSerializer valueSerializer = extractorsContainer.getValueProcessor(serialization.getPropertyClass());
    serialization.setValueSerializer(valueSerializer);
    serialization.setPropertyType(PropertyType.SIMPLE);
    return serialization;
  }

  private PropertySerialization createComplexObjectSerialization(PropertySerialization parentSerialization, PropertySerialization serialization, SerializationInput input) {
    if (Collection.class.isAssignableFrom(serialization.getPropertyClass())) {
      createCollectionSerialization(parentSerialization, serialization, input);
    } else if (serialization.getPropertyClass().isArray()) {
      createArraySerialization(serialization, input);
    } else if (Map.class.isAssignableFrom(serialization.getPropertyClass())) {
      createMapSerialization(parentSerialization, serialization, input);
    } else if (serialization.getPropertyClass().isEnum()) {
      createEnumSerialization(serialization);
    } else {
      createBeanObjectSerialization(serialization, input);
    }
    return serialization;
  }

  @SuppressWarnings("unchecked")
  private void createEnumSerialization(PropertySerialization serialization) {
    Class<Enum> enumClass = (Class<Enum>) serialization.getPropertyClass();
    ValueSerializer serializer;
    if (extractorsContainer.containsEnumSerializer(enumClass)) {
      serializer = extractorsContainer.getEnumSerializer(enumClass);
    } else {
      serializer = createNewEnumSerializer(enumClass);
      extractorsContainer.addEnumSerialization(enumClass, serializer);
    }
    serialization.setValueSerializer(serializer);
    serialization.setPropertyType(PropertyType.SIMPLE);
  }

  private ValueSerializer createNewEnumSerializer(Class<Enum> enumClass) {
    Map<String, Enum> enumValues = new HashMap<String, Enum>();
    for (Enum enumValue : enumClass.getEnumConstants()) {
      enumValues.put(enumValue.name(), enumValue);
    }
    return new EnumSerializer(enumValues);
  }

  private PropertySerialization createBeanObjectSerialization(PropertySerialization propertySerialization, SerializationInput input) {
    propertySerialization.setPropertyType(PropertyType.BEAN);
    if (propertyCannotBeInstantiated(propertySerialization)) {
      propertySerialization.setUsingImplementations();
      return propertySerialization;
    }
    List<PropertyAccess> properties = setChildrenProperties(propertySerialization);

    int requiredChildren = 0;
    for (PropertyAccess property : properties) {
      Type childType = resolveChildType(input, propertySerialization, property.getPropertyField());
      SerializationInput childInput = resolveChildInput(input, property);
      PropertySerialization childPropertySerialization = doResolveSerialization(propertySerialization, childType, childInput);
      if (childPropertySerialization == null) {
        propertySerialization.getReadableProperties().remove(property.getFieldName());
        propertySerialization.getWritableProperties().remove(property.getFieldName());
      } else {
        propertySerialization.addSerializedProperty(property.getFieldName(), childPropertySerialization);
        if (childPropertySerialization.getConstraints().isRequired()) {
          requiredChildren++;
        }
      }
    }
    propertySerialization.getConstraints().setRequiredChildren(requiredChildren);
    return propertySerialization;
  }

  private boolean propertyCannotBeInstantiated(PropertySerialization propertySerialization) {
    return propertySerialization.getPropertyClass().isInterface() || Modifier.isAbstract(propertySerialization.getPropertyClass().getModifiers());
  }

  private List<PropertyAccess> setChildrenProperties(PropertySerialization propertySerialization) {
    List<PropertyAccess> properties = PropertyUtils.getClassProperties(propertySerialization.getPropertyClass(), true);
    Map<String, PropertyAccess> readableProperties = new HashMap<String, PropertyAccess>();
    Map<String, PropertyAccess> writableProperties = new HashMap<String, PropertyAccess>();
    for (PropertyAccess property : properties) {
      if (property.isReadable()) {
        readableProperties.put(property.getFieldName(), property);
      }
      if (property.isWritable()) {
        writableProperties.put(property.getFieldName(), property);
      }
    }
    propertySerialization.setReadableProperties(readableProperties);
    propertySerialization.setWritableProperties(writableProperties);
    return properties;
  }

  private Type resolveChildType(SerializationInput parentInput, PropertySerialization propertySerialization, Field propertyField) {
    SerializationInput childInput = parentInput.getNonDefaultProperties().get(propertyField.getName());
    if (childInput != null && childInput.hasSpecifiedType()) {
      return resolveType(propertySerialization, childInput.getPropertyInformation().getType());
    }
    Type genericReturnType = propertyField.getGenericType();
    return resolveType(propertySerialization, genericReturnType);
  }

  private Type resolveType(PropertySerialization propertySerialization, Type genericReturnType) {
    PropertySerialization basicInformation = resolveBasicInformation(propertySerialization, genericReturnType);
    if (basicInformation.getGenericType() != null) {
      return basicInformation.getGenericType();
    }
    return basicInformation.getPropertyClass();
  }

  private SerializationInput resolveChildInput(SerializationInput input, PropertyAccess property) {
    SerializationInput childInput = input.getNonDefaultProperties().get(property.getFieldName());
    if (childInput == null) {
      return new SerializationInput();
    }
    return childInput;
  }

  private void createCollectionSerialization(PropertySerialization parentSerialization, PropertySerialization serialization, SerializationInput input) {
    serialization.setPropertyType(PropertyType.COLLECTION);
    Type elementType = getCollectionElementType(parentSerialization, serialization, input);
    if (elementType != null) {
      PropertySerialization elementSerialization = doResolveSerialization(serialization, elementType, input);
      serialization.addSerializedProperty(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME, elementSerialization);
    }
  }

  private Type getCollectionElementType(PropertySerialization parentSerialization, PropertySerialization propertySerialization, SerializationInput input) {
    return getContainerElementType(parentSerialization, propertySerialization, input, ReflectionUtils.COLLECTION_ELEMENT_PARAMETER_INDEX);
  }

  private void createArraySerialization(PropertySerialization propertySerialization, SerializationInput input) {
    //todo- resolve generic array type
    propertySerialization.setPropertyType(PropertyType.ARRAY);
    Type elementType = propertySerialization.getPropertyClass().getComponentType();
    PropertySerialization elementSerialization = doResolveSerialization(propertySerialization, elementType, input);
    propertySerialization.addSerializedProperty(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME, elementSerialization);
  }

  private void createMapSerialization(PropertySerialization parentSerialization, PropertySerialization propertySerialization, SerializationInput input) {
    propertySerialization.setPropertyType(PropertyType.MAP);
    Type defaultEntryValueType = getMapEntryValueType(parentSerialization, propertySerialization, input);
    if (defaultEntryValueType != null) {
      PropertySerialization elementSerialization = doResolveSerialization(propertySerialization, defaultEntryValueType, input);
      propertySerialization.addSerializedProperty(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME, elementSerialization);
    }
    for (Map.Entry<String, SerializationInput> inputEntry : input.getNonDefaultProperties().entrySet()) {
      if (!inputEntry.getKey().equals(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME)) {
        SerializationInput entryInput = inputEntry.getValue();
        Type entryValueType = getEntryValueType(defaultEntryValueType, entryInput);

        PropertySerialization elementSerialization = doResolveSerialization(propertySerialization, entryValueType, entryInput);
        propertySerialization.addSerializedProperty(inputEntry.getKey(), elementSerialization);
      }
    }
  }

  private Type getEntryValueType(Type defaultEntryValueType, SerializationInput entryInput) {
    if (entryInput.hasSpecifiedType()) {
      return entryInput.getPropertyInformation().getType();
    }
    return defaultEntryValueType;
  }

  private Type getMapEntryValueType(PropertySerialization parentSerialization, PropertySerialization propertySerialization, SerializationInput input) {
    return getContainerElementType(parentSerialization, propertySerialization, input, ReflectionUtils.MAP_ELEMENT_PARAMETER_INDEX);
  }

  private Type getContainerElementType(PropertySerialization parentSerialization, PropertySerialization propertySerialization, SerializationInput input, int parameterIndexInType) {
    if (propertySerialization.getGenericType() != null) {
      ParameterizedType parametrizedCollection = (ParameterizedType) propertySerialization.getGenericType();
      Type containerElementType = parametrizedCollection.getActualTypeArguments()[parameterIndexInType];
      return resolveType(parentSerialization, containerElementType);
    }
    Class elementClass = input.getPropertyInformation().getElementClass();
    if (elementClass == null || elementClass.equals(Object.class)) {
      log.debug("Error while resolving container element type, such information is not available. \n "
        + "Provide parametrised type of container or specify container element type via " + Serialize.class.getSimpleName() + " annotation");
      return null;
    }
    return input.getPropertyInformation().getElementClass();
  }

  private boolean isPropertySkipped(SerializationInput input) {
    PropertyInformation propertyInput = input.getPropertyInformation();
    return propertyInput != null && propertyInput.isSkipped();
  }

  private boolean isDefaultSerializationInContainer(Type objectType, SerializationInput input) {
    return !input.containsNonDefaultProperties() && extractorsContainer.containsPropertySerialization(objectType);
  }

  public PropertySerialization resolveImplementationSerialization(Class<?> implementationClass, PropertySerialization abstractInformation) {
    PropertySerialization implementationSerialization = doResolveSerialization(abstractInformation, implementationClass, abstractInformation.getSerializationInput());
    abstractInformation.getImplementationSerializations().put(implementationClass, implementationSerialization);

    return implementationSerialization;
  }
}
