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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class that holds information about object setter.
 * Contains information about setter method and field name.
 * <p/>
 * Created on: 2011-02-23 11:40:30 <br/>
 *
 * @author Wojciech Niemiec
 */
public class Setter {

  private String fieldName;
  private Method setterMethod;

  /**
   * Creates a new Setter object with complete information.
   *
   * @param fieldName    name of the field.
   * @param setterMethod getter method.
   */
  public Setter(String fieldName, Method setterMethod) {
    this.fieldName = fieldName;
    this.setterMethod = setterMethod;
  }

  /**
   * Invokes getter method on the object given as parameter.
   *
   * @param bean  the object the underlying method is invoked from.
   * @param value value of field.
   * @throws IllegalAccessException    exception thrown for further processing.
   * @throws InvocationTargetException exception thrown for further processing.
   */
  public void invokeWithoutCheck(Object bean, Object value) throws IllegalAccessException, InvocationTargetException {
    setterMethod.invoke(bean, value);
  }

  /**
   * Gets the type of field.
   *
   * @return type of object set by this setter method.
   */
  public Class getFieldType() {
    return setterMethod.getParameterTypes()[0];
  }

  /**
   * Name of the returned field name.
   *
   * @return name of the returned field name.
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * Getter method.
   *
   * @return getter method.
   */
  public Method getSetterMethod() {
    return setterMethod;
  }
}
