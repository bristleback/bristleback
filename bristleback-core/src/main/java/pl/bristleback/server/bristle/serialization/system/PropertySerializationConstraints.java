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

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-15 10:14:19 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PropertySerializationConstraints {

  private boolean required;
  private int requiredChildren;
  private boolean detailedErrors;
  private Object formatHolder;

  public boolean isFormatted() {
    return formatHolder != null;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public boolean requiresDetailedErrors() {
    return detailedErrors;
  }

  public void setDetailedErrors(boolean detailedErrors) {
    this.detailedErrors = detailedErrors;
  }

  public Object getFormatHolder() {
    return formatHolder;
  }

  public void setFormatHolder(Object formatHolder) {
    this.formatHolder = formatHolder;
  }

  public int getRequiredChildren() {
    return requiredChildren;
  }

  public void setRequiredChildren(int requiredChildren) {
    this.requiredChildren = requiredChildren;
  }
}
