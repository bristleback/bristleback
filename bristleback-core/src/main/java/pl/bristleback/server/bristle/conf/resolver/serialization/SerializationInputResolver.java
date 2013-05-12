/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.conf.resolver.serialization;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.serialization.system.PropertyInformation;
import pl.bristleback.server.bristle.serialization.system.SerializationInput;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.bristle.serialization.system.annotation.Property;
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;
import pl.bristleback.server.bristle.utils.StringUtils;

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
    propertyInformation.setFormat(bindAnnotation.format());
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
    propertyInformation.setFormat(nonDefaultProperty.format());
    propertyInformation.setRequired(nonDefaultProperty.required());
    propertyInformation.setSkipped(nonDefaultProperty.skipped());
  }

  private void initRootSerializeInformation(Serialize serializeAnnotation, SerializationInput input) {
    PropertyInformation propertyInformation = resolvePropertyInformation(input, StringUtils.EMPTY);
    propertyInformation.setRequired(serializeAnnotation.required());
    propertyInformation.setFormat(serializeAnnotation.format());
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
}
