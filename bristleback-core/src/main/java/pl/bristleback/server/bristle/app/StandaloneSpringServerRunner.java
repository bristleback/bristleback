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

package pl.bristleback.server.bristle.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pl.bristleback.server.bristle.BristleRuntimeException;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.conf.BristleInitializationException;
import pl.bristleback.server.bristle.conf.resolver.ServerInstanceResolver;
import pl.bristleback.server.bristle.conf.resolver.spring.SpringApplicationComponentsContainer;

/**
 * This is a "main" class in standalone websocket applications.
 * Instance of this class is created by <code>&lt;bb:standaloneServer&gt;</code> tag.
 * Each standalone server runner can be used to start only one server instance.
 * To start server, one should set <code>startAfterInit</code> property to <code>true</code> in
 * <code>&lt;bb:standaloneServer&gt;</code> tag or retrieve {@link StandaloneSpringServerRunner} bean from the
 * Spring Application Context and run {@link StandaloneSpringServerRunner#startServer()} method.
 * To stop the standalone server, invoke {@link StandaloneSpringServerRunner#stopServer()} method.
 * <p/>
 * Created on: 2011-09-26 22:00:49 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class StandaloneSpringServerRunner implements ApplicationContextAware {

  private InitialConfigurationResolver initialConfigurationResolver;

  private BristlebackServerInstance serverInstance;

  private ApplicationContext actualApplicationContext;

  /**
   * Starts standalone server.
   */
  public void startServer() {
    if (initialConfigurationResolver == null) {
      throw new BristleInitializationException("Cannot start Bristleback Server, missing initialConfiguration resolver");
    } else if (serverInstance != null) {
      throw new BristleInitializationException("Cannot start Bristleback Server, create a new ServerRunner instance to start new server.");
    }

    SpringApplicationComponentsContainer componentsResolver = new SpringApplicationComponentsContainer(actualApplicationContext);
    ServerInstanceResolver serverInstanceResolver = new ServerInstanceResolver(initialConfigurationResolver, componentsResolver);
    serverInstance = serverInstanceResolver.resolverServerInstance();

    serverInstance.startServer();
  }

  /**
   * Stops standalone server.
   */
  public void stopServer() {
    if (serverInstance == null || !serverInstance.isRunning()) {
      throw new BristleRuntimeException("Cannot stop Bristleback server, server is not up.");
    }
    serverInstance.stopServer();
  }

  public BristlebackServerInstance getServerInstance() {
    return serverInstance;
  }

  public void setInitialConfigurationResolver(InitialConfigurationResolver initialConfigurationResolver) {
    this.initialConfigurationResolver = initialConfigurationResolver;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.actualApplicationContext = applicationContext;
  }
}