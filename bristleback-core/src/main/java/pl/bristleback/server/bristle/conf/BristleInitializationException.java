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

package pl.bristleback.server.bristle.conf;

/**
 * This exception is thrown during server initialization.
 * <p/>
 * Created on: 2011-07-03 23:51:08 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristleInitializationException extends RuntimeException {

  public BristleInitializationException(String s) {
    super(s);
  }

  public BristleInitializationException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
