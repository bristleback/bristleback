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
import pl.bristleback.server.bristle.action.extractor.ActionExtractorsContainer;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.BristleRuntimeException;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-08 14:44:34 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ActionExtractorsResolver {

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  @Named(SpringConfigurationResolver.CONFIG_BEAN_NAME)
  private BristlebackConfig configuration;

  public ActionExtractorsContainer resolveParameterExtractors() {
    Map<Class, ActionParameterExtractor> extractors = new HashMap<Class, ActionParameterExtractor>();
    resolveDefaultParameterExtractors(extractors);
    resolveCustomParameterExtractors(extractors);

    ActionExtractorsContainer actionExtractorsContainer = new ActionExtractorsContainer();
    actionExtractorsContainer.setParameterExtractors(extractors);
    initParameterExtractors(actionExtractorsContainer);
    return actionExtractorsContainer;
  }

  private void initParameterExtractors(ActionExtractorsContainer actionExtractorsContainer) {
    for (ActionParameterExtractor extractor : actionExtractorsContainer.getParameterExtractors().values()) {
      extractor.init(configuration);
    }
  }

  private void resolveCustomParameterExtractors(Map<Class, ActionParameterExtractor> extractors) {
    Map<String, ActionParameterExtractor> extractorInstances = springIntegration.getApplicationBeansOfType(ActionParameterExtractor.class);
    addExtractors(extractors, extractorInstances, ActionParameterExtractor.class);
  }

  private void resolveDefaultParameterExtractors(Map<Class, ActionParameterExtractor> extractors) {
    Map<String, ActionParameterExtractor> extractorInstances = springIntegration.getFrameworkBeansOfType(ActionParameterExtractor.class);
    addExtractors(extractors, extractorInstances, ActionParameterExtractor.class);
  }

  private <T> void addExtractors(Map<Class, T> extractors, Map<String, T> extractorsContainer, Class<T> parametrizedInterface) {
    for (T extractor : extractorsContainer.values()) {
      Class extractorClass = extractor.getClass();
      Class parameterClass = getParameterClass(extractorClass, parametrizedInterface);
      extractors.put(parameterClass, extractor);
      Class primitiveForParameterClass = ReflectionUtils.getPrimitiveForWrapper(parameterClass);
      if (primitiveForParameterClass != null && !extractors.containsKey(primitiveForParameterClass)) {
        extractors.put(primitiveForParameterClass, extractor);
      }
    }
  }

  private Class getParameterClass(Class extractorClass, Class parametrizedInterface) {
    for (int i = 0; i < extractorClass.getInterfaces().length; i++) {
      Class interfaceOfClass = extractorClass.getInterfaces()[i];
      if (parametrizedInterface.equals(interfaceOfClass)) {
        Type genericInterface = extractorClass.getGenericInterfaces()[i];
        if (genericInterface instanceof ParameterizedType) {
          return (Class) ((ParameterizedType) (genericInterface)).getActualTypeArguments()[0];
        }
        return Object.class;
      }
    }
    throw new BristleRuntimeException("Cannot find " + parametrizedInterface.getSimpleName() + " interface, this exception should never happen.");
  }
}
