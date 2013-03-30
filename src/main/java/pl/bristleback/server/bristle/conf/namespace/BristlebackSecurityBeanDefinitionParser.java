package pl.bristleback.server.bristle.conf.namespace;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
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
    addBasicBeans(parserContext);
    addAuthenticationBeans(element, parserContext);
    addAuthorizationBeans(element, parserContext);
  }

  private void addAuthorizationBeans(Element element, ParserContext parserContext) {
    BeanDefinition authorizationInterceptor = BeanDefinitionBuilder
      .rootBeanDefinition(AuthorizationInterceptor.class)
      .getBeanDefinition();
    registerBean(parserContext, authorizationInterceptor, "bristleAuthorizationInterceptor");

    BeanDefinition authorizationInterceptorContextResolver = BeanDefinitionBuilder
      .rootBeanDefinition(AuthorizationInterceptorContextResolver.class)
      .getBeanDefinition();
    registerBean(parserContext, authorizationInterceptorContextResolver, "bristleAuthorizationInterceptorContextResolver");
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

  private void addBasicBeans(ParserContext parserContext) {
    BeanDefinition authenticationConfiguration = BeanDefinitionBuilder
      .rootBeanDefinition(AuthenticationConfiguration.class)
      .getBeanDefinition();
    registerBean(parserContext, authenticationConfiguration, "bristleAuthenticationConfiguration");

    BeanDefinition authenticationsContainer = BeanDefinitionBuilder
      .rootBeanDefinition(AuthenticationsContainer.class)
      .getBeanDefinition();
    registerBean(parserContext, authenticationsContainer, "bristleAuthenticationsContainer");

    BeanDefinition securityExceptionHandler = BeanDefinitionBuilder
      .rootBeanDefinition(BristleSecurityExceptionHandler.class)
      .getBeanDefinition();
    registerBean(parserContext, securityExceptionHandler, "bristleSecurityExceptionHandler");
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
