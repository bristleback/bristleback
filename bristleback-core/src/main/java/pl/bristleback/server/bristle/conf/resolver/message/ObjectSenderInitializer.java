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

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.security.UsersContainer;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class simply initializes given object sender, by passing them configuration and serialization bundle.
 * {@link pl.bristleback.server.bristle.api.SerializationResolver} implementation is used to resolve serialization bundle.
 * <p/>
 * Created on: 2012-06-23 14:31:29 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ObjectSenderInitializer {

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Inject
  private UsersContainer connectedUsers;

  public void initObjectSender(BristlebackConfig configuration, ConditionObjectSender objectSender) {
    objectSender.init(configuration, connectedUsers);
    resolveSerializations(objectSender);
  }

  private void resolveSerializations(ConditionObjectSender objectSender) {
    SerializationBundle serializationBundle = serializationEngine.getSerializationResolver().initSerializationBundle(objectSender.getField());
    objectSender.setLocalSerializations(serializationBundle);
  }
}
