package pl.bristleback.server.bristle.conf.namespace;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import pl.bristleback.server.bristle.app.StandaloneServerRunner;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-08-18 17:57:27 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristlebackStandaloneServerBeanDefinitionParser extends BaseBristlebackBeanDefinitionParser {

  public static final String DEFAULT_STANDALONE_SERVER_NAME = "bristlebackStandaloneServer";

  protected Class getBeanClass(Element element) {
    return StandaloneServerRunner.class;
  }

  protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
    String id = super.resolveId(element, definition, parserContext);
    if (StringUtils.isNotBlank(id)) {
      return id;
    }
    if (parserContext.getRegistry().isBeanNameInUse(DEFAULT_STANDALONE_SERVER_NAME)) {
      throw new BristleInitializationException("Multiple Bristleback Servers with default name. \n"
        + "Please specify name of the server runner bean.");
    }

    return DEFAULT_STANDALONE_SERVER_NAME;
  }

  @Override
  protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    boolean startAfterInit = Boolean.valueOf(element.getAttribute("startAfterInit"));
    String configurationResolver = element.getAttribute("configurationResolver");
    builder.addPropertyReference("initialConfigurationResolver", configurationResolver);
    if (startAfterInit) {
      builder.setInitMethodName("startServer");
    }
  }
}
