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

package pl.bristleback.server.bristle.security.authentication;

/**
 * This enumeration informs user why his authentication has become invalidated.
 * <p/>
 * Created on: 26.03.13 20:29 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum LogoutReason {

  TOO_MANY_AUTHENTICATIONS,
  REQUESTED_BY_CLIENT
}
