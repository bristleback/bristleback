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
