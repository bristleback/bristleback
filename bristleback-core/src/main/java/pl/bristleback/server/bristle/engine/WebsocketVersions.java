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

package pl.bristleback.server.bristle.engine;

/**
 * Mapping between Websocket Draft/Specification version and value of <code>Sec-WebSocket-Version</code> header
 * that should be sent by the client on opening handshake to be successfully handled by the server.
 * <p/>
 * Created on: 2011-09-25 15:30:12 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum WebsocketVersions {

  HIXIE_76("0"),
  HYBI_10("8"),
  HYBI_13("13"),
  RFC_6455("13");

  private String versionCode;

  WebsocketVersions(String versionCode) {
    this.versionCode = versionCode;
  }

  public String getVersionCode() {
    return versionCode;
  }
}