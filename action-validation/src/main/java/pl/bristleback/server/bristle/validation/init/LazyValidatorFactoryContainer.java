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

package pl.bristleback.server.bristle.validation.init;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.conf.BristleInitializationException;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

import javax.inject.Inject;
import javax.validation.ValidatorFactory;
import java.util.Map;

@Component
public class LazyValidatorFactoryContainer implements ApplicationContextAware {

  private static Logger log = Logger.getLogger(LazyValidatorFactoryContainer.class.getName());

  private ApplicationContext applicationContext;

  @Inject
  private HibernateSimpleValidatorInitializer defaultInitializer;

  private ValidatorFactory validatorFactory;

  public ValidatorFactory getOrBuildValidatorFactory() {
    if (validatorFactory == null) {
      validatorFactory = buildValidatorFactory();
    }
    return validatorFactory;
  }

  private ValidatorFactory buildValidatorFactory() {
    BristleSpringIntegration springIntegration = applicationContext.getBean("springIntegration", BristleSpringIntegration.class);
    Map<String, ValidatorFactoryInitializer> customFactories = springIntegration.getApplicationBeansOfType(ValidatorFactoryInitializer.class);
    ValidatorFactoryInitializer factoryInitializer;
    if (customFactories.isEmpty()) {
      factoryInitializer = defaultInitializer;
    } else if (customFactories.size() > 1) {
      throw new BristleInitializationException("Multiple custom validator factory initializers.");
    } else {
      factoryInitializer = customFactories.values().iterator().next();
    }

    return factoryInitializer.createValidatorFactory();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
