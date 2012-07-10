package pl.bristleback.server.bristle.conf.resolver.serialization;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.annotations.Bind;
import pl.bristleback.server.bristle.api.annotations.Property;
import pl.bristleback.server.bristle.api.annotations.Serialize;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.PropertyInformation;
import pl.bristleback.server.bristle.serialization.SerializationInput;
import pl.bristleback.server.bristle.utils.StringUtils;

import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-13 15:59:22 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class SerializationInputResolver {

  public SerializationInput resolveInputInformation(Bind bindAnnotation) {
    Property[] nonDefaultProperties = bindAnnotation.properties();
    SerializationInput input = new SerializationInput();
    PropertyInformation propertyInformation = resolvePropertyInformation(input, StringUtils.EMPTY);
    propertyInformation.setDetailedErrors(bindAnnotation.detailedErrors());
    propertyInformation.setRequired(bindAnnotation.required());
    for (Property nonDefaultProperty : nonDefaultProperties) {
      processPropertyAnnotation(input, nonDefaultProperty);
    }
    return input;
  }

  public SerializationInput resolveInputInformation(Serialize serializeAnnotation) {
    Property[] nonDefaultProperties = serializeAnnotation.properties();

    SerializationInput input = new SerializationInput();
    initRootSerializeInformation(serializeAnnotation, input);
    for (Property nonDefaultProperty : nonDefaultProperties) {
      processPropertyAnnotation(input, nonDefaultProperty);
    }
    return input;
  }

  private void processPropertyAnnotation(SerializationInput input, Property nonDefaultProperty) {
    String propertyName = nonDefaultProperty.name();
    String[] propertyPath = StringUtils.getPropertyChain(propertyName);

    SerializationInput inputForName = input;
    for (String aPropertyPath : propertyPath) {
      inputForName = resolveInputForName(inputForName, aPropertyPath);
    }
    PropertyInformation propertyInformation = resolvePropertyInformation(inputForName, propertyPath[propertyPath.length - 1]);
    setInformationFromAnnotation(nonDefaultProperty, propertyInformation);
  }

  private void setInformationFromAnnotation(Property nonDefaultProperty, PropertyInformation propertyInformation) {
    propertyInformation.setRequired(nonDefaultProperty.required());
    propertyInformation.setSkipped(nonDefaultProperty.skipped());
  }

  private void initRootSerializeInformation(Serialize serializeAnnotation, SerializationInput input) {
    PropertyInformation propertyInformation = resolvePropertyInformation(input, StringUtils.EMPTY);
    propertyInformation.setRequired(serializeAnnotation.required());
    propertyInformation.setElementClass(serializeAnnotation.containerElementClass());
  }

  private SerializationInput resolveInputForName(SerializationInput serializationInput, String propertyName) {
    SerializationInput input = serializationInput.getNonDefaultProperties().get(propertyName);
    if (input == null) {
      input = new SerializationInput();
      serializationInput.getNonDefaultProperties().put(propertyName, input);
    }
    return input;
  }

  private PropertyInformation resolvePropertyInformation(SerializationInput inputForName, String propertyPath) {
    PropertyInformation propertyInformation = inputForName.getPropertyInformation();
    if (propertyInformation == null) {
      propertyInformation = new PropertyInformation();
      inputForName.setPropertyInformation(propertyInformation);
      propertyInformation.setName(propertyPath);
    }
    return propertyInformation;
  }

  public SerializationInput resolveMessageInputInformation(Type contentType, SerializationInput contentInput) {
    SerializationInput serializationInput = new SerializationInput();
    PropertyInformation contentPropertyInformation = resolvePropertyInformation(contentInput, BristleMessage.PAYLOAD_PROPERTY_NAME);
    contentPropertyInformation.setType(contentType);

    serializationInput.getNonDefaultProperties().put(BristleMessage.PAYLOAD_PROPERTY_NAME, contentInput);

    return serializationInput;
  }
}
