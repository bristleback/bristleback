package pl.bristleback.server.bristle.conf.namespace;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import pl.bristleback.server.bristle.authentication.AuthenticatingAction;

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
    boolean useDefaultAuthenticationAction = element.hasAttribute("userDetailsService");
    if (useDefaultAuthenticationAction) {
      String userDetailsServiceBeanName = element.getAttribute("userDetailsService");
      BeanDefinition authenticationAction = BeanDefinitionBuilder
        .rootBeanDefinition(AuthenticatingAction.class)
        .addPropertyReference("userDetailsService", userDetailsServiceBeanName)
        .getBeanDefinition();
      registerBean(parserContext, authenticationAction, "bristleSystemUserAuthentication");
    }

  }

}
