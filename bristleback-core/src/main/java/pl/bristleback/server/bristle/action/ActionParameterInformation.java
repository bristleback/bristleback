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

import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 17:38:15 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionParameterInformation {

  private Type parameterType;
  private List<Annotation> parameterAnnotations;
  private Object propertySerialization;
  private ActionParameterExtractor extractor;

  public ActionParameterInformation(Type parameterType, Annotation[] parameterAnnotations) {
    this.parameterType = parameterType;
    this.parameterAnnotations = Arrays.asList(parameterAnnotations);
  }

  public Object resolveParameter(String message, ActionExecutionContext context) throws Exception {
    return extractor.fromTextContent(message, this, context);
  }

  public ActionParameterExtractor getExtractor() {
    return extractor;
  }

  public void setExtractor(ActionParameterExtractor extractor) {
    this.extractor = extractor;
  }

  public Object getPropertySerialization() {
    return propertySerialization;
  }

  public void setPropertySerialization(Object propertySerialization) {
    this.propertySerialization = propertySerialization;
  }

  public Type getParameterType() {
    return parameterType;
  }

  public List<Annotation> getParameterAnnotations() {
    return parameterAnnotations;
  }

  @Override
  public String toString() {
    return "Action parameter information, extractor: " + extractor + ", property Serialization: " + propertySerialization;
  }
}
