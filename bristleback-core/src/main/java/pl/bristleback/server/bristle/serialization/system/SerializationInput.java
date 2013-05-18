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

import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-08-31 17:00:15 <br/>
 *
 * @author Wojciech Niemiec
 */
public class SerializationInput {

  private Map<String, SerializationInput> nonDefaultProperties = new HashMap<String, SerializationInput>();

  private PropertyInformation propertyInformation;

  private boolean detailedErrors;

  public boolean hasSpecifiedType() {
    return propertyInformation != null && propertyInformation.getType() != null;
  }

  public boolean containsNonDefaultProperties() {
    return propertyInformation != null || !nonDefaultProperties.isEmpty();
  }

  public Map<String, SerializationInput> getNonDefaultProperties() {
    return nonDefaultProperties;
  }

  public PropertyInformation getPropertyInformation() {
    return propertyInformation;
  }

  public void setPropertyInformation(PropertyInformation propertyInformation) {
    this.propertyInformation = propertyInformation;
  }

  public boolean isDetailedErrors() {
    return detailedErrors;
  }

  public void setDetailedErrors(boolean detailedErrors) {
    this.detailedErrors = detailedErrors;
  }
}