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

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-26 12:42:24 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PropertyInformation {

  private boolean detailedErrors;
  private boolean required;
  private boolean skipped;
  private String name;

  private Type type;
  private Class elementClass;
  private String format;

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public boolean isSkipped() {
    return skipped;
  }

  public void setSkipped(boolean skipped) {
    this.skipped = skipped;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Class getElementClass() {
    return elementClass;
  }

  public void setElementClass(Class elementClass) {
    this.elementClass = elementClass;
  }

  public boolean isDetailedErrors() {
    return detailedErrors;
  }

  public void setDetailedErrors(boolean detailedErrors) {
    this.detailedErrors = detailedErrors;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }
}