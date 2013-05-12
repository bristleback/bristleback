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

package pl.bristleback.server.bristle;

/**
 * This is a main runtime exception thrown by Bristleback elements. It is thrown because of programmer mistakes
 * or because of lack of implementation. It is recommended to not catch bristleback runtime exceptions.
 * <p/>
 * Created on: 2010-10-05 00:58:12 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristleRuntimeException extends RuntimeException {

  public BristleRuntimeException(String message) {
    super(message);
  }

  public BristleRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
