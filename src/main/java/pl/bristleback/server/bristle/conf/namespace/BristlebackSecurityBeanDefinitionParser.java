package pl.bristleback.server.bristle.conf.namespace;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import pl.bristleback.server.bristle.authentication.AuthenticatingAction;
import pl.bristleback.server.bristle.authentication.AuthenticationConfiguration;
import pl.bristleback.server.bristle.authentication.AuthenticationInterceptor;
import pl.bristleback.server.bristle.authentication.AuthenticationInterceptorContextResolver;
import pl.bristleback.server.bristle.authentication.AuthenticationsContainer;
import pl.bristleback.server.bristle.authentication.LogoutAction;
import pl.bristleback.server.bristle.authentication.LogoutInterceptor;

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
    addInterceptorBeans(parserContext);

    boolean useDefaultAuthenticationAction = element.hasAttribute("userDetailsService");
    if (useDefaultAuthenticationAction) {
      registerAuthenticationActionBean(element, parserContext);
    }
    boolean useDefaultLogoutAction = Boolean.valueOf(element.getAttribute("useDefaultLogoutAction"));
    if (useDefaultLogoutAction) {
      registerLogoutAction(parserContext);
    }
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
