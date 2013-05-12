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

package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.ConfigurationAware;

/**
 * In addition to custom value serializers, action controller uses parameter extractors for resolving action parameters
 * that can’t be resolved using serialization engine. Similar to exception handlers,
 * the most suitable parameter extractor is chosen for each parameter at server start.
 * T means the most generic parameter type that can be extracted by the implementation.
 * This can be tricky because if the extractor handles parameters of type A and the particular action method parameter is of type B extends A,
 * that extractor will be invoked to resolve parameter and it must return object of B type,
 * otherwise ClassCastException will be thrown. To recognise type of the parameter,
 * {@link ActionParameterExtractor#fromTextContent(String, pl.bristleback.server.bristle.action.ActionParameterInformation,
 * pl.bristleback.server.bristle.action.ActionExecutionContext) fromTextContent()}
 * method contains ActionParameterInformation parameter, which provides information about currently extracted action parameter.
 * <p/>
 * Created on: 2011-07-14 15:11:28 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionParameterExtractor<T> extends ConfigurationAware {

  /**
   * Retrieves action parameters using text sent by client, action parameter information and context of the
   * currently executed action.
   *
   * @param text                 serialized version of the currently processed parameter.
   * @param parameterInformation meta information about currently processed parameter.
   * @param context              action execution context. If {@link pl.bristleback.server.bristle.api.action.ActionParameterExtractor#isDeserializationRequired()}
   *                             returns true, serialization of that parameter is resolved using {@link pl.bristleback.server.bristle.api.SerializationResolver}.
   * @return resolved parameter.
   * @throws Exception thrown if any problem occurs.
   */
  T fromTextContent(String text, ActionParameterInformation parameterInformation, ActionExecutionContext context) throws Exception;

  /**
   * This flag indicates whether {@link pl.bristleback.server.bristle.api.SerializationResolver} implementation will be used
   * to retrieve serialization information of processed parameter type.
   * In case when serialization resolver is used, serialization information is stored in {@link ActionParameterInformation} object.
   *
   * @return true if serialization resolver must be used, false otherwise.
   */
  boolean isDeserializationRequired();
}
