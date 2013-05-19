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

import java.lang.reflect.Field;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-14 20:50:40 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PropertyAccess {

  private Field propertyField;
  private Getter propertyGetter;
  private Setter propertySetter;

  public PropertyAccess(Field propertyField) {
    this.propertyField = propertyField;
  }

  public Field getPropertyField() {
    return propertyField;
  }

  public String getFieldName() {
    return propertyField.getName();
  }

  public Getter getPropertyGetter() {
    return propertyGetter;
  }

  public Setter getPropertySetter() {
    return propertySetter;
  }

  public void setPropertyGetter(Getter propertyGetter) {
    this.propertyGetter = propertyGetter;
  }

  public void setPropertySetter(Setter propertySetter) {
    this.propertySetter = propertySetter;
  }

  public boolean isReadable() {
    return propertyGetter != null;
  }

  public boolean isWritable() {
    return propertySetter != null;
  }
}
