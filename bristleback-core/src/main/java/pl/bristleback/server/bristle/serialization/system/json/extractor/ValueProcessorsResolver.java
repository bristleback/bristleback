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

package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.serialization.system.ExtractorsContainer;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-08 14:24:12 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ValueProcessorsResolver {

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  @Named(SpringConfigurationResolver.CONFIG_BEAN_NAME)
  private BristlebackConfig configuration;

  public void resolvePropertyValueExtractors(ExtractorsContainer extractorsContainer) {
    Map<Class, ValueSerializer> extractors = new HashMap<Class, ValueSerializer>();
    resolveDefaultValueExtractors(extractors);
    resolveCustomValueExtractors(extractors);
    for (ValueSerializer extractor : extractors.values()) {
      extractor.init(configuration);
    }
    extractorsContainer.setValueProcessors(extractors);
  }

  private void resolveDefaultValueExtractors(Map<Class, ValueSerializer> extractors) {
    Map<String, ValueSerializer> extractorInstances = springIntegration.getFrameworkBeansOfType(ValueSerializer.class);
    addExtractors(extractors, extractorInstances);
  }

  private void resolveCustomValueExtractors(Map<Class, ValueSerializer> extractors) {
    Map<String, ValueSerializer> extractorInstances = springIntegration.getApplicationBeansOfType(ValueSerializer.class);
    addExtractors(extractors, extractorInstances);
  }

  private <T> void addExtractors(Map<Class, T> extractors, Map<String, T> extractorsContainer) {
    for (T extractor : extractorsContainer.values()) {
      Class extractorClass = extractor.getClass();
      Class parameterClass = (Class) ReflectionUtils.getParameterTypes(extractorClass, ValueSerializer.class)[0];
      extractors.put(parameterClass, extractor);
      Class primitiveForParameterClass = ReflectionUtils.getPrimitiveForWrapper(parameterClass);
      if (primitiveForParameterClass != null) {
        extractors.put(primitiveForParameterClass, extractor);
      }
    }
  }
}
