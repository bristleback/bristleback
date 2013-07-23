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

package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.serialization.SerializationBundle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Serialization information resolvers cooperate with {@link SerializationEngine} implementations
 * by providing meta information about serialized/deserialized type for them.
 * Type of serialization information object is determined by the serialization engine cooperating with this
 * serialization resolver implementation.
 * Serialization resolvers must provide functionality of processing
 * {@link pl.bristleback.server.bristle.message.ConditionObjectSender} fields and initializing
 * {@link SerializationBundle} object.
 * <p/>
 * Created on: 2012-01-08 14:07:10 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface SerializationResolver<T> extends ConfigurationAware {

  SerializationBundle initSerializationBundle(Field objectSenderField);

  T resolveSerialization(Type objectType, Annotation... annotations);
}
