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

package pl.bristleback.server.bristle.conf.namespace;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import pl.bristleback.server.bristle.security.UserDetailsParameterExtractor;
import pl.bristleback.server.bristle.security.authentication.AuthenticatingAction;
import pl.bristleback.server.bristle.security.authentication.AuthenticationConfiguration;
import pl.bristleback.server.bristle.security.authentication.AuthenticationInformer;
import pl.bristleback.server.bristle.security.authentication.AuthenticationInterceptor;
import pl.bristleback.server.bristle.security.authentication.AuthenticationInterceptorContextResolver;
import pl.bristleback.server.bristle.security.authentication.AuthenticationsContainer;
import pl.bristleback.server.bristle.security.authentication.LogoutAction;
import pl.bristleback.server.bristle.security.authentication.LogoutInterceptor;
import pl.bristleback.server.bristle.security.authentication.UserDisconnectedListener;
import pl.bristleback.server.bristle.security.authorisation.interceptor.AuthorizationInterceptor;
import pl.bristleback.server.bristle.security.authorisation.interceptor.AuthorizationInterceptorContextResolver;
import pl.bristleback.server.bristle.security.exception.handler.AuthorizationExceptionHandler;
import pl.bristleback.server.bristle.security.exception.handler.BristleSecurityExceptionHandler;

/**
 * Parser for Bristleback security tag.
 * <p/>
 * Created on: 23.02.13 17:56 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristlebackSecurityBeanDefinitionParser extends BaseBristlebackBeanDefinitionParser {

  protected Class getBeanClass(Element element) {
    return String.class;
  }

  protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
    return "bristle.system.server.security.id";
  }

  @Override
  protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    addBasicBeans(element, parserContext);
    addAuthenticationBeans(element, parserContext);
    addAuthorizationBeans(parserContext);
  }

  private void addAuthorizationBeans(ParserContext parserContext) {
    BeanDefinition authorizationInterceptor = BeanDefinitionBuilder
      .rootBeanDefinition(AuthorizationInterceptor.class)
      .getBeanDefinition();
    registerBean(parserContext, authorizationInterceptor, "bristleAuthorizationInterceptor");

    BeanDefinition authorizationInterceptorContextResolver = BeanDefinitionBuilder
      .rootBeanDefinition(AuthorizationInterceptorContextResolver.class)
      .getBeanDefinition();
    registerBean(parserContext, authorizationInterceptorContextResolver, "bristleAuthorizationInterceptorContextResolver");

    BeanDefinition authorizationExceptionHandler = BeanDefinitionBuilder
      .rootBeanDefinition(AuthorizationExceptionHandler.class)
      .getBeanDefinition();
    registerBean(parserContext, authorizationExceptionHandler, "bristleAuthorizationExceptionHandler");
  }

  private void addAuthenticationBeans(Element element, ParserContext parserContext) {
    addInterceptorBeans(parserContext);

    boolean useDefaultAuthenticationAction = element.hasAttribute("userDetailsService");
    if (useDefaultAuthenticationAction) {
      registerAuthenticationActionBean(element, parserContext);
    }
    boolean useDefaultLogoutAction = Boolean.valueOf(element.getAttribute("useDefaultLogoutAction"));
    if (useDefaultLogoutAction) {
      registerLogoutAction(parserContext);
    }
    registerAuthenticationInformer(parserContext);

    registerUserDisconnectionListener(parserContext);
  }

  private void registerAuthenticationInformer(ParserContext parserContext) {
    BeanDefinition disconnectionListener = BeanDefinitionBuilder
      .rootBeanDefinition(AuthenticationInformer.class)
      .getBeanDefinition();
    registerBean(parserContext, disconnectionListener, "bristleAuthenticationInformer");
  }

  private void registerUserDisconnectionListener(ParserContext parserContext) {
    BeanDefinition disconnectionListener = BeanDefinitionBuilder
      .rootBeanDefinition(UserDisconnectedListener.class)
      .getBeanDefinition();
    registerBean(parserContext, disconnectionListener, "bristleAuthenticationUserDisconnectedListener");
  }

  private void addBasicBeans(Element element, ParserContext parserContext) {
    registerSecurityConfigurationBean(element, parserContext);

    BeanDefinition authenticationsContainer = BeanDefinitionBuilder
      .rootBeanDefinition(AuthenticationsContainer.class)
      .getBeanDefinition();
    registerBean(parserContext, authenticationsContainer, "bristleAuthenticationsContainer");

    BeanDefinition securityExceptionHandler = BeanDefinitionBuilder
      .rootBeanDefinition(BristleSecurityExceptionHandler.class)
      .getBeanDefinition();
    registerBean(parserContext, securityExceptionHandler, "bristleSecurityExceptionHandler");

    BeanDefinition userDetailsParameterExtractor = BeanDefinitionBuilder
      .rootBeanDefinition(UserDetailsParameterExtractor.class)
      .getBeanDefinition();
    registerBean(parserContext, userDetailsParameterExtractor, "bristleUserDetailsParameterExtractor");
  }

  private void registerSecurityConfigurationBean(Element element, ParserContext parserContext) {
    Integer maxConcurrentConnectionsPerUsername = Integer.parseInt(element.getAttribute("maxConcurrentPerUsername"));
    BeanDefinition authenticationConfiguration = BeanDefinitionBuilder
      .rootBeanDefinition(AuthenticationConfiguration.class)
      .addPropertyValue("maximumAuthenticationsPerUsername", maxConcurrentConnectionsPerUsername)
      .getBeanDefinition();
    registerBean(parserContext, authenticationConfiguration, "bristleAuthenticationConfiguration");
  }

  private void addInterceptorBeans(ParserContext parserContext) {
    BeanDefinition authenticationInterceptor = BeanDefinitionBuilder
      .rootBeanDefinition(AuthenticationInterceptor.class)
      .getBeanDefinition();
    registerBean(parserContext, authenticationInterceptor, "bristleAuthenticationInterceptor");

    BeanDefinition authenticationInterceptorContextResolver = BeanDefinitionBuilder
      .rootBeanDefinition(AuthenticationInterceptorContextResolver.class)
      .getBeanDefinition();
    registerBean(parserContext, authenticationInterceptorContextResolver, "bristleAuthenticationInterceptorContextResolver");

    BeanDefinition logoutInterceptor = BeanDefinitionBuilder
      .rootBeanDefinition(LogoutInterceptor.class)
      .getBeanDefinition();
    registerBean(parserContext, logoutInterceptor, "bristleLogoutInterceptor");

  }

  private void registerAuthenticationActionBean(Element element, ParserContext parserContext) {
    String userDetailsServiceBeanName = element.getAttribute("userDetailsService");
    BeanDefinition authenticationAction = BeanDefinitionBuilder
      .rootBeanDefinition(AuthenticatingAction.class)
      .addPropertyReference("userDetailsService", userDetailsServiceBeanName)
      .getBeanDefinition();
    registerBean(parserContext, authenticationAction, "bristleSystemUserAuthentication");
  }

  private void registerLogoutAction(ParserContext parserContext) {
    BeanDefinition logoutAction = BeanDefinitionBuilder
      .rootBeanDefinition(LogoutAction.class)
      .getBeanDefinition();
    registerBean(parserContext, logoutAction, "bristleSystemUserLogoutAction");
  }

}
