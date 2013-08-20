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

package pl.bristleback.server.bristle.conf.resolver.message;

import org.apache.commons.lang.reflect.FieldUtils;
import org.springframework.beans.BeanInstantiationException;
import pl.bristleback.server.bristle.api.annotations.ObjectSender;
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
public abstract class ObjectSenderInjector {

  private RegisteredObjectSenders registeredObjectSenders;

  public ObjectSenderInjector(RegisteredObjectSenders registeredObjectSenders) {
    this.registeredObjectSenders = registeredObjectSenders;
  }

  public void injectSenders(Object component) {
    Field[] fields = component.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(ObjectSender.class)) {
        ConditionObjectSender objectSender = resolveSender(field);
        injectProperty(component, field, objectSender);
      }
    }
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
    ConditionObjectSender conditionObjectSender = getObjectSenderInstance();
    conditionObjectSender.setField(field);
    SerializationBundle serializationBundle = new SerializationBundle();

    conditionObjectSender.setLocalSerializations(serializationBundle);
    registeredObjectSenders.putSender(field, conditionObjectSender);

    return conditionObjectSender;
  }

  protected abstract ConditionObjectSender getObjectSenderInstance();
}

