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

import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * Data controller processes incoming text and binary data.
 * Each non control message sent by user is forwarded to data controller implementation.
 * There might be multiple active data controllers, from which connecting clients choose one they wish to communicate with.
 * <p/>
 * Created on: 2011-07-19 15:18:15 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.action.ActionController Action Data Controller
 */
public interface DataController extends ConfigurationAware {

  /**
   * Processes incoming, serialized text data, sent by given user.
   *
   * @param textData    text data.
   * @param userContext user that sent the message.
   */
  void processTextData(String textData, UserContext userContext);

  /**
   * Processes incoming, serialized binary data, sent by given user.
   *
   * @param binaryData  binary data.
   * @param userContext user that sent the message.
   */
  void processBinaryData(byte[] binaryData, UserContext userContext);
} 