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
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.conf.ConfigurationElementResolver;

import javax.inject.Inject;
import javax.validation.ValidatorFactory;

@Component
public class LazyValidatorFactoryContainer {

  private static Logger log = Logger.getLogger(LazyValidatorFactoryContainer.class.getName());

  @Inject
  private ConfigurationElementResolver elementResolver;

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
    ValidatorFactoryInitializer factoryInitializer = elementResolver.getConfigurationElement(ValidatorFactoryInitializer.class, defaultInitializer);

    return factoryInitializer.createValidatorFactory();
  }
}
