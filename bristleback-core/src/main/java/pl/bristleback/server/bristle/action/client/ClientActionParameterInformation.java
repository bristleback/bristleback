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

package pl.bristleback.server.bristle.action.client;

import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-17 20:41:06 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionParameterInformation {

  private Type parameterType;
  private boolean forSerialization;
  private Object serialization;


  private ClientActionParameterInformation(Type parameterType, boolean forSerialization) {
    this.parameterType = parameterType;
    this.forSerialization = forSerialization;
  }

  public ClientActionParameterInformation(Type parameterType) {
    this(parameterType, false);
  }

  public ClientActionParameterInformation(Type parameterType, Object serialization) {
    this(parameterType, true);
    this.serialization = serialization;
  }

  public Type getParameterType() {
    return parameterType;
  }

  public boolean isForSerialization() {
    return forSerialization;
  }

  public Object getSerialization() {
    return serialization;
  }

  @Override
  public String toString() {
    return "client action parameter, type: " + parameterType + ", for serialization: " + forSerialization;
  }
}
