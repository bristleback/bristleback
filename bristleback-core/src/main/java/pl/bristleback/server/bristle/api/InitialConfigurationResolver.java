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

package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.conf.InitialConfiguration;

/**
 * Implementations of this interface resolve configuration settings from source provided by application creator.
 * <p/>
 * Created on: 2011-09-26 22:15:58 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface InitialConfigurationResolver {

  /**
   * Resolves configuration settings.
   * This method should return complete configuration settings object.
   *
   * @return configuration settings object.
   */
  InitialConfiguration resolveConfiguration();
} 
