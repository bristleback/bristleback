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

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import pl.bristleback.server.bristle.action.client.ClientActionProxyInterceptor;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.conf.resolver.message.RegisteredObjectSenders;
import pl.bristleback.server.bristle.conf.resolver.spring.ObjectSenderInjector;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-08-18 08:59:51 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristlebackServerMessagesBeanDefinitionParser extends BaseBristlebackBeanDefinitionParser {

  protected Class getBeanClass(Element element) {
    return String.class;
  }

  protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
    return "bristle.system.server.messages.id";
  }

  @Override
  protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    registerConditionSenderBean(parserContext);
    registerObjectSendersContainer(parserContext);
    registerConditionSenderBeanPostProcessor(parserContext);

    registerClientActionClasses(parserContext);
  }

  private void registerClientActionClasses(ParserContext parserContext) {
    BeanDefinition autoProxyCreator = BeanDefinitionBuilder
      .rootBeanDefinition(DefaultAdvisorAutoProxyCreator.class)
      .getBeanDefinition();
    registerBean(parserContext, autoProxyCreator, "autoProxyCreator");

    BeanDefinition clientActionClassPointcut = BeanDefinitionBuilder
      .rootBeanDefinition(AnnotationClassFilter.class)
      .addConstructorArgValue(ClientActionClass.class)
      .getBeanDefinition();
    registerBean(parserContext, clientActionClassPointcut, "outputAnnotationPointcut");

    BeanDefinition clientActionPointcut = BeanDefinitionBuilder
      .rootBeanDefinition(AnnotationMethodMatcher.class)
      .addConstructorArgValue(ClientAction.class)
      .getBeanDefinition();
    registerBean(parserContext, clientActionPointcut, "outputActionAnnotationPointcut");

    BeanDefinition clientActionMechanismPointcut = BeanDefinitionBuilder
      .rootBeanDefinition(ComposablePointcut.class)
      .addConstructorArgReference("outputAnnotationPointcut")
      .addConstructorArgReference("outputActionAnnotationPointcut")
      .getBeanDefinition();
    registerBean(parserContext, clientActionMechanismPointcut, "clientActionMechanismPointcut");

    BeanDefinition clientActionInterceptor = BeanDefinitionBuilder
      .rootBeanDefinition(ClientActionProxyInterceptor.class)
      .getBeanDefinition();
    registerBean(parserContext, clientActionInterceptor, "clientActionInterceptor");

    BeanDefinition clientActionMessageProxyAdvisor = BeanDefinitionBuilder
      .rootBeanDefinition(DefaultPointcutAdvisor.class)
      .addPropertyReference("pointcut", "clientActionMechanismPointcut")
      .addPropertyReference("advice", "clientActionInterceptor")
      .getBeanDefinition();
    registerBean(parserContext, clientActionMessageProxyAdvisor, "clientActionMessageProxyAdvisor");
  }

  private void registerObjectSendersContainer(ParserContext parserContext) {
    registerRootBean(parserContext, RegisteredObjectSenders.class, RegisteredObjectSenders.COMPONENT_NAME, BeanDefinition.SCOPE_SINGLETON);
  }

  private void registerConditionSenderBeanPostProcessor(ParserContext parserContext) {
    BeanDefinition conditionSenderBeanPostProcessor = BeanDefinitionBuilder
      .rootBeanDefinition(ObjectSenderInjector.class)
      .addPropertyReference("registeredObjectSenders", RegisteredObjectSenders.COMPONENT_NAME)
      .getBeanDefinition();
    registerBean(parserContext, conditionSenderBeanPostProcessor, "conditionSenderBeanPostProcessor");
  }

  private void registerConditionSenderBean(ParserContext parserContext) {
    registerRootBean(parserContext, ConditionObjectSender.class, "system.sender.condition", BeanDefinition.SCOPE_PROTOTYPE);
  }
}
