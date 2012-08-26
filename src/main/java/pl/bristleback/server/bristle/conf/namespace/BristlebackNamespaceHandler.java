package pl.bristleback.server.bristle.conf.namespace;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-08-18 08:59:12 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristlebackNamespaceHandler extends NamespaceHandlerSupport {
  public void init() {
    registerBeanDefinitionParser("serverMessages", new BristlebackServerMessagesBeanDefinitionParser());
    registerBeanDefinitionParser("standaloneServer", new BristlebackStandaloneServerBeanDefinitionParser());
    registerBeanDefinitionParser("servlet", new BristlebackServletBeanDefinitionParser());
  }
}
