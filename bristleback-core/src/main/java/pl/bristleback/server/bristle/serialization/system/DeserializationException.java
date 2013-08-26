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

package pl.bristleback.server.bristle.serialization.system;

/**
 * Exception thrown during deserialization process performed by
 * {@link pl.bristleback.server.bristle.serialization.RawMessageSerializationEngine} or {@link pl.bristleback.server.bristle.serialization.system.converter.JsonTokenizer}.
 * It does not show the exact place in which the problem occurs, the reason for the exception is written as exception message.
 * <p/>
 * Created on: 05.01.13 13:26 <br/>
 *
 * @author Wojciech Niemiec
 */
public class DeserializationException extends RuntimeException {

  public DeserializationException(String message) {
    super(message);
  }
}
