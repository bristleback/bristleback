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

package pl.bristleback.server.bristle.conf.resolver.action;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.action.extractor.ActionExtractorsContainer;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.serialization.SerializationResolvingException;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-23 13:13:41 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ParameterResolver {

  @Inject
  private ActionExtractorsResolver actionExtractorsResolver;

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  private ActionExtractorsContainer actionExtractorsContainer;

  @PostConstruct
  private void init() {
    actionExtractorsContainer = actionExtractorsResolver.resolveParameterExtractors();
  }

  public ActionParameterInformation prepareActionParameter(Type parameterType, Annotation[] parameterAnnotations) {
    ActionParameterInformation parameterInformation = new ActionParameterInformation(parameterType, parameterAnnotations);

    resolveParamExtractor(parameterInformation, parameterType);
    resolveParameterDetails(parameterInformation, parameterType, parameterAnnotations);
    return parameterInformation;
  }

  private void resolveParameterDetails(ActionParameterInformation parameterInformation, Type parameterType, Annotation[] parameterAnnotations) {
    SerializationResolver serializationResolver = serializationEngine.getSerializationResolver();
    Object propertySerialization = null;

    if (parameterInformation.getExtractor().isDeserializationRequired()) {
      propertySerialization = serializationResolver.resolveSerialization(parameterType, parameterAnnotations);
    }
    parameterInformation.setPropertySerialization(propertySerialization);
  }

  private void resolveParamExtractor(ActionParameterInformation parameterInformation, Type parameterType) {
    Class parameterClass;
    if (parameterType instanceof ParameterizedType) {
      parameterClass = (Class) (((ParameterizedType) parameterType).getRawType());
    } else if (parameterType instanceof Class) {
      parameterClass = (Class) parameterType;
    } else {
      throw new SerializationResolvingException("Incompatible type, cannot resolve parameter extractor: " + parameterType);
    }

    parameterClass = getParameterClass(parameterClass);
    ActionParameterExtractor extractor = actionExtractorsContainer.getParameterExtractor(parameterClass);
    parameterInformation.setExtractor(extractor);
  }

  private Class getParameterClass(Class parameterClass) {
    if (parameterClass.isPrimitive()) {
      return ReflectionUtils.getWrapperClassForPrimitive(parameterClass);
    } else {
      return parameterClass;
    }
  }
}