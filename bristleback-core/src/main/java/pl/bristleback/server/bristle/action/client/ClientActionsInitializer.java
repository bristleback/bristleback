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

package pl.bristleback.server.bristle.action.client;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.security.UsersContainer;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import javax.inject.Inject;

/**
 * This component initializes client action classes proxy.
 * <p/>
 * Created on: 2012-07-08 21:03:42 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionsInitializer {

  @Inject
  private ClientActionClasses clientActionClasses;

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  private UsersContainer usersContainer;

  public void initActionClasses(BristlebackConfig configuration) {
    ClientActionProxyInterceptor proxyInterceptor = getClientActionInterceptor();
    if (proxyInterceptor == null) {
      // client actions not enabled
      return;
    }
    SerializationBundle serializationBundle = new SerializationBundle();
    ConditionObjectSender objectSender = initObjectSender(configuration, serializationBundle);

    proxyInterceptor.init(springIntegration, objectSender);
  }

  private ClientActionProxyInterceptor getClientActionInterceptor() {
    try {
      return springIntegration.getApplicationBean(ClientActionProxyInterceptor.class);
    } catch (NoSuchBeanDefinitionException e) {
      return null;
    }
  }

  private ConditionObjectSender initObjectSender(BristlebackConfig configuration, SerializationBundle serializationBundle) {
    ConditionObjectSender objectSender = new ConditionObjectSender();
    objectSender.init(configuration, usersContainer);
    objectSender.setLocalSerializations(serializationBundle);
    return objectSender;
  }
}
