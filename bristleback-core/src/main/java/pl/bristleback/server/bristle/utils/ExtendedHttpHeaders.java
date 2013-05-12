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

package pl.bristleback.server.bristle.utils;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-14 11:16:53 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class ExtendedHttpHeaders {

  private ExtendedHttpHeaders() {
    throw new UnsupportedOperationException();
  }

  public static final class Names {

    private Names() {
      throw new UnsupportedOperationException();
    }

    public static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
    public static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
    public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
  }


}