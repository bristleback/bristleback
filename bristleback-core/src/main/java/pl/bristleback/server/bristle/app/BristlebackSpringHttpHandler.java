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
import org.springframework.web.HttpRequestHandler;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.ServletServerEngine;
import pl.bristleback.server.bristle.conf.BristleInitializationException;
import pl.bristleback.server.bristle.conf.resolver.ServerInstanceResolver;
import pl.bristleback.server.bristle.conf.resolver.spring.SpringApplicationComponentsResolver;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Basic Spring Http handler used in all Bristleback Web applications.
 * To make use of this class, application creator has to use
 * <a href="http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html">
 * Spring Dispatcher Servlet</a> and add it to <code>web.xml</code> descriptor file.
 * Details of how it can be done are presented on the
 * <a href="https://github.com/bristleback/bristleback/wiki/Project-setup#wiki-Project_setup__Web_application">Bristleback wiki page</a>.
 * Bristleback <code>&lt;bb:servlet&gt;</code> tag creates Spring bean of this class and maps it in
 * Spring <code>HandlerMapping</code> implementation. If not provided by application creator, servlet tag creates following mapping:
 * <br>
 * <code> <br>
 * &nbsp;&nbsp;&lt;bean id="customMappings" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping"&gt; <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="mappings"&gt; <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;map&gt; <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;entry key="&#42;&#42;/&#42;" value-ref="bristlebackHttpHandler"/&gt; <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/map&gt; <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/property&gt; <br>
 * &nbsp;&nbsp;&lt;/bean&gt; <br>
 * </code>
 * Which means that every request within Spring Dispatcher Servlet will be handled by Bristleback Http handler.
 * Application creator can specify custom url handler mappings. To use custom url handler mapping,
 * just set <code>handlerMappings</code> field in servlet tag.
 * <p/>
 * If Bristleback Http handler is used,
 * only those Websocket Engines that implement {@link ServletServerEngine} interface can be used,
 * as their {@link ServletServerEngine#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) handleRequest()}
 * method is used here.
 * <p/>
 * Created on: 2012-04-25 16:59:49 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristlebackSpringHttpHandler implements HttpRequestHandler, ApplicationContextAware {

  private InitialConfigurationResolver initialConfigurationResolver;

  private ApplicationContext applicationContext;

  private BristlebackServerInstance serverInstance;

  private ServletServerEngine servletServerEngine;

  @PostConstruct
  public void startServer() {
    SpringApplicationComponentsResolver componentsResolver = new SpringApplicationComponentsResolver(applicationContext);
    ServerInstanceResolver serverInstanceResolver = new ServerInstanceResolver(initialConfigurationResolver, componentsResolver);
    serverInstance = serverInstanceResolver.resolverServerInstance();

    setServletEngine(serverInstance.getConfiguration());
    serverInstance.startServer();
  }

  protected void setServletEngine(BristlebackConfig configuration) {
    ServerEngine serverEngine = configuration.getServerEngine();
    if (!(serverEngine instanceof ServletServerEngine)) {
      throw new BristleInitializationException("Cannot start Bristleback servlet WebSockets engine. "
        + "Given engine does not implement " + ServletServerEngine.class.getName() + " interface.");
    }
    servletServerEngine = (ServletServerEngine) serverEngine;
  }

  @PreDestroy
  public void stopServer() {
    serverInstance.stopServer();
  }

  @Override
  public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    servletServerEngine.handleRequest(request, response);
  }

  public void setInitialConfigurationResolver(InitialConfigurationResolver initialConfigurationResolver) {
    this.initialConfigurationResolver = initialConfigurationResolver;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
