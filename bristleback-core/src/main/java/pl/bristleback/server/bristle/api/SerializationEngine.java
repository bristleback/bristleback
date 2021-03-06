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

/**
 * This interface provides functionality for converting between java objects/primitives and other formats, like JSON or XML.
 * Implementations should provide both deserialization and serialization operations.
 * Serialization in Bristleback server is a two-phase operation. To improve speed of serialization,
 * information about serialization process may be cached using {@link pl.bristleback.server.bristle.api.SerializationResolver} implementation.
 * Such serialization information is used later to serialize or deserialize object.
 * The way serialization engine will make use of serialization information resolver depends on this engine.
 * Serialization engine is one of the most important parts of the server, used in data controllers and sending server messages.
 *
 * @param <T> type of serialization information object.
 */
public interface SerializationEngine<T> extends ConfigurationAware {

  /**
   * Gets an instance of serialization resolver object, used to retrieve serialization information.
   *
   * @return serialization resolver.
   */
  SerializationResolver<T> getSerializationResolver();

  /**
   * Converts String into java object, using given serialization information.
   * Deserialization process requires serialization information (for example, to know the type of deserialized object).
   *
   * @param serializedObject serialized form of object.
   * @param serialization    serialization information.
   * @return Object with the correct state.
   * @throws Exception if any exception while deserialization occurs.
   */
  Object deserialize(String serializedObject, T serialization) throws Exception;

  /**
   * Serializes object using given serialization information object.
   *
   * @param object        object to serialize.
   * @param serialization serialization information.
   * @return object in serialized form.
   * @throws Exception if any exception while serialization occurs.
   */
  String serialize(Object object, T serialization) throws Exception;

  /**
   * Serializes object using default settings (with default serialization object or without any information).
   *
   * @param object object to serialize.
   * @return object in serialized form.
   * @throws Exception if any exception while serialization occurs.
   */
  String serialize(Object object) throws Exception;
}
