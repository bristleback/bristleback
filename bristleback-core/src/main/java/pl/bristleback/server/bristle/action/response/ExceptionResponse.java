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

package pl.bristleback.server.bristle.action.response;

import pl.bristleback.server.bristle.action.ActionExecutionStage;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-30 19:53:04 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ExceptionResponse {

  private static final String UNKNOWN_EXCEPTION = "UnknownException";

  private String type;
  private String stage;

  public ExceptionResponse() {
    this(UNKNOWN_EXCEPTION, ActionExecutionStage.ACTION_EXECUTION);
  }

  public ExceptionResponse(ActionExecutionStage stage) {
    this(UNKNOWN_EXCEPTION, stage);
  }

  public ExceptionResponse(String type) {
    this(type, ActionExecutionStage.ACTION_EXECUTION);
  }

  public ExceptionResponse(String type, ActionExecutionStage stage) {
    this.stage = stage.name();
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }
}
