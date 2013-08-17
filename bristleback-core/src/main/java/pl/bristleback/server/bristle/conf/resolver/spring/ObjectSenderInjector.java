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

package pl.bristleback.server.bristle.conf.resolver.spring;

import org.apache.commons.lang.reflect.FieldUtils;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
import pl.bristleback.server.bristle.conf.resolver.message.RegisteredObjectSenders;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import java.lang.reflect.Field;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-11 13:45:23 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ObjectSenderInjector implements BeanPostProcessor, ApplicationContextAware {

  private ApplicationContext applicationContext;

  private RegisteredObjectSenders registeredObjectSenders;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    Field[] fields = bean.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(ObjectSender.class)) {
        ConditionObjectSender objectSender = resolveSender(field);
        injectProperty(bean, field, objectSender);
      }
    }
    return bean;
  }

  private void injectProperty(Object bean, Field field, ConditionObjectSender objectSender) {
    try {
      FieldUtils.writeDeclaredField(bean, field.getName(), objectSender, true);
    } catch (IllegalAccessException e) {
      throw new BeanInstantiationException(bean.getClass(), e.getMessage(), e);
    }
  }

  private ConditionObjectSender resolveSender(Field field) {
    if (registeredObjectSenders.containsSenderForField(field)) {
      return registeredObjectSenders.getSender(field);
    }
    ConditionObjectSender conditionObjectSender = applicationContext.getBean(ConditionObjectSender.class);
    conditionObjectSender.setField(field);
    SerializationBundle serializationBundle = new SerializationBundle();

    conditionObjectSender.setLocalSerializations(serializationBundle);
    registeredObjectSenders.putSender(field, conditionObjectSender);

    return conditionObjectSender;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public void setRegisteredObjectSenders(RegisteredObjectSenders registeredObjectSenders) {
    this.registeredObjectSenders = registeredObjectSenders;
  }
}

