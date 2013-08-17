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

package pl.bristleback.server.bristle.conf.resolver;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.bristleback.server.bristle.api.ApplicationComponentsContainer;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.conf.BristlebackComponentsContainer;
import pl.bristleback.server.bristle.conf.InitialConfiguration;
import pl.bristleback.server.bristle.conf.resolver.spring.BristlebackBeanFactoryPostProcessor;
import pl.bristleback.server.bristle.conf.resolver.spring.SpringConfigurationResolver;

/**
 * This component resolves Bristleback Server instance and initializes internal Bristleback Spring context.
 * <p/>
 * <p/>
 * Created on: 2012-05-01 16:47:44 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ServerInstanceResolver {

  private static Logger log = Logger.getLogger(ServerInstanceResolver.class.getName());

  private InitialConfigurationResolver initialConfigurationResolver;

  private ApplicationComponentsContainer applicationComponentsContainer;

  public ServerInstanceResolver(InitialConfigurationResolver initialConfigurationResolver,
                                ApplicationComponentsContainer applicationComponentsContainer) {
    this.initialConfigurationResolver = initialConfigurationResolver;
    this.applicationComponentsContainer = applicationComponentsContainer;
  }

  public BristlebackServerInstance resolverServerInstance() {
    InitialConfiguration initialConfiguration = initialConfigurationResolver.resolveConfiguration();
    startLogger(initialConfiguration);

    AnnotationConfigApplicationContext frameworkContext = new AnnotationConfigApplicationContext();
    BristlebackComponentsContainer springIntegration = new BristlebackComponentsContainer(applicationComponentsContainer, frameworkContext);
    BristlebackBeanFactoryPostProcessor bristlebackPostProcessor = new BristlebackBeanFactoryPostProcessor(initialConfiguration, springIntegration);
    frameworkContext.addBeanFactoryPostProcessor(bristlebackPostProcessor);
    frameworkContext.register(SpringConfigurationResolver.class);
    frameworkContext.scan(InitialConfiguration.SYSTEM_BASE_PACKAGES);
    frameworkContext.refresh();

    BristlebackConfig configuration = frameworkContext.getBean("bristlebackConfigurationFinal", BristlebackConfig.class);
    return new BristlebackServerInstance(configuration);
  }

  private void startLogger(InitialConfiguration initialConfiguration) {
    if (initialConfiguration.getLoggingLevel() != null) {
      BasicConfigurator.configure();
      Logger.getRootLogger().setLevel(initialConfiguration.getLoggingLevel());
      Logger.getLogger("org.apache").setLevel(initialConfiguration.getLoggingLevel());
      if (log.isInfoEnabled()) {
        Logger.getLogger("org.springframework.beans.factory").setLevel(Level.WARN);
        Logger.getLogger("org.springframework.context.support").setLevel(Level.WARN);
        Logger.getLogger("org.springframework.context.annotation").setLevel(Level.WARN);
        Logger.getLogger("org.springframework.core.io.support").setLevel(Level.WARN);
      }
    }
  }
}
