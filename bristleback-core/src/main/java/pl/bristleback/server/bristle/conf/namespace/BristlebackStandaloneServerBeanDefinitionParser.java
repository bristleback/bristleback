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
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import pl.bristleback.server.bristle.app.StandaloneServerRunner;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

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
