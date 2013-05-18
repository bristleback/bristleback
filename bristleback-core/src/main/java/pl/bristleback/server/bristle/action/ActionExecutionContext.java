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

package pl.bristleback.server.bristle.action;

import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.action.exception.BrokenActionProtocolException;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.utils.StringUtils;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-04 14:47:51 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionExecutionContext {

  private ActionExecutionStage stage;

  private BristleMessage<String[]> message;

  private UserContext userContext;

  private String actionClassName;

  private String actionName;

  private ActionInformation action;

  private Object[] actionParameters;

  private Object response;

  private boolean responseSendingCancelled;

  public ActionExecutionContext(UserContext userContext) {
    stage = ActionExecutionStage.MESSAGE_DESERIALIZATION;
    this.userContext = userContext;
  }

  public void extractActionInformation() {
    if (org.apache.commons.lang.StringUtils.isEmpty(message.getId())) {
      throw new BrokenActionProtocolException(BrokenActionProtocolException.ReasonType.NO_MESSAGE_ID_FOUND,
        "Request Id cannot be null.");
    }
    setStage(ActionExecutionStage.ACTION_EXTRACTION);
    if (message.getName().contains(StringUtils.DOT_AS_STRING)) {
      int dotIndex = message.getName().indexOf(StringUtils.DOT);
      actionClassName = message.getName().substring(0, dotIndex);
      actionName = message.getName().substring(dotIndex + 1);
    } else {
      actionClassName = message.getName();
      actionName = StringUtils.EMPTY;
    }
  }

  public String getActionClassName() {
    return actionClassName;
  }

  public String getActionName() {
    return actionName;
  }

  public ActionExecutionStage getStage() {
    return stage;
  }

  public void setStage(ActionExecutionStage stage) {
    this.stage = stage;
  }

  public BristleMessage<String[]> getMessage() {
    return message;
  }

  public void setMessage(BristleMessage<String[]> message) {
    this.message = message;
  }

  public UserContext getUserContext() {
    return userContext;
  }

  public ActionInformation getAction() {
    return action;
  }

  public void setAction(ActionInformation action) {
    this.action = action;
  }

  public Object[] getActionParameters() {
    return actionParameters;
  }

  public void setActionParameters(Object[] actionParameters) {
    this.actionParameters = actionParameters;
  }

  public Object getResponse() {
    return response;
  }

  public void setResponse(Object response) {
    this.response = response;
  }

  public boolean isResponseSendingCancelled() {
    return responseSendingCancelled;
  }

  public void cancelResponseSending() {
    this.responseSendingCancelled = true;
  }
}
