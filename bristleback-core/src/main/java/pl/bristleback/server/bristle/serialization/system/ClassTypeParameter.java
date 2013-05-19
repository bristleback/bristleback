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

package pl.bristleback.server.bristle.serialization.system;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-02 14:19:59 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClassTypeParameter {

  private TypeVariable parameterName;

  private Type parameterType;

  public ClassTypeParameter(TypeVariable parameterName, Type parameterType) {
    this.parameterName = parameterName;
    this.parameterType = parameterType;
  }

  public TypeVariable getParameterName() {
    return parameterName;
  }

  public Type getParameterType() {
    return parameterType;
  }
}
