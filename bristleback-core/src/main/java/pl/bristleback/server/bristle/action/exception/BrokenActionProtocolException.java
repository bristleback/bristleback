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

package pl.bristleback.server.bristle.action.exception;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 09:11:13 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BrokenActionProtocolException extends RuntimeException {

  private ReasonType reasonType;

  public BrokenActionProtocolException(ReasonType reasonType, String message) {
    super(message);
    this.reasonType = reasonType;
  }

  public ReasonType getReasonType() {
    return reasonType;
  }

  public static enum ReasonType {
    INCORRECT_PATH,
    NO_MESSAGE_ID_FOUND,
    NO_ACTION_CLASS_FOUND,
    NO_DEFAULT_ACTION_FOUND,
    NO_ACTION_FOUND,
    TOO_FEW_ACTION_PARAMETERS
  }
}