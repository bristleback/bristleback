package pl.bristleback.server.bristle.conf.namespace;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;

/**
 * Base class for built in Bristleback tags parsers.
 * <p/>
 * Created on: 23.02.13 18:01 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class BaseBristlebackBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

  protected BeanDefinition registerRootBean(ParserContext parserContext, Class<?> clazz, String beanName, String scope) {
    BeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(clazz).setScope(scope).getBeanDefinition();
    registerBean(parserContext, beanDefinition, beanName);
    return beanDefinition;
  }

  protected void registerBean(ParserContext parserContext, BeanDefinition beanDefinition, String beanName) {
    BeanComponentDefinition component = new BeanComponentDefinition(beanDefinition, beanName);
    parserContext.registerBeanComponent(component);
  }


}
