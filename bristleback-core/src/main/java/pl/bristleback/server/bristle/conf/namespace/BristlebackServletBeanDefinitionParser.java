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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.w3c.dom.Element;
import pl.bristleback.server.bristle.engine.servlet.BristlebackHttpHandler;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-08-19 15:01:22 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristlebackServletBeanDefinitionParser extends BaseBristlebackBeanDefinitionParser {

  public static final String DEFAULT_HTTP_HANDLER_NAME = "bristlebackHttpHandler";

  public static final String DEFAULT_HANDLER_MAPPINGS_NAME = "bristlebackHandlerMappings";  //handlerMapping

  protected Class getBeanClass(Element element) {
    return BristlebackHttpHandler.class;
  }

  protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
    String id = super.resolveId(element, definition, parserContext);
    if (StringUtils.isNotBlank(id)) {
      return id;
    }
    if (parserContext.getRegistry().isBeanNameInUse(DEFAULT_HTTP_HANDLER_NAME)) {
      throw new BristleInitializationException("Multiple Bristleback Servers with default name. \n"
        + "Please specify name of the server runner bean.");
    }

    return DEFAULT_HTTP_HANDLER_NAME;
  }

  @Override
  protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    String configurationResolver = element.getAttribute("configurationResolver");
    builder.addPropertyReference("initialConfigurationResolver", configurationResolver);

    checkHandlerMappings(element, parserContext, builder);
  }

  private void checkHandlerMappings(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    String customHandlerMappings = element.getAttribute("handlerMappings");
    if (StringUtils.isBlank(customHandlerMappings)) {
      if (parserContext.getRegistry().isBeanNameInUse(DEFAULT_HANDLER_MAPPINGS_NAME)) {
        throw new BristleInitializationException("Multiple Bristleback Servlets with default handler mappings. \n"
          + "Please provide custom handler mappings.");
      }

      createDefaultMappings(element, parserContext, builder);
    }
  }

  private void createDefaultMappings(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    Map<String, Object> mappings = new HashMap<String, Object>();
    String handlerBeanId = resolveId(element, builder.getRawBeanDefinition(), parserContext);
    mappings.put("**/*", handlerBeanId);

    BeanDefinition defaultMappingsBean = BeanDefinitionBuilder
      .rootBeanDefinition(SimpleUrlHandlerMapping.class)
      .addPropertyValue("urlMap", mappings)
      .getBeanDefinition();
    registerBean(parserContext, defaultMappingsBean, DEFAULT_HANDLER_MAPPINGS_NAME);
  }

}
