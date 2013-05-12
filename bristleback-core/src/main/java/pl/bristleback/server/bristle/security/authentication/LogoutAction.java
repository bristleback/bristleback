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

package pl.bristleback.server.bristle.security.authentication;

import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Intercept;

/**
 * This is a default logout action class used in system authentication framework.
 * Its only action, {@link pl.bristleback.server.bristle.security.authentication.LogoutAction#logout()}
 * is intercepted by {@link LogoutInterceptor}, which invalidates current authentication from actual connection.
 * <p/>
 * Created on: 18.02.13 20:30 <br/>
 *
 * @author Wojciech Niemiec
 */
@ActionClass(name = "BristleSystemUserLogout")
public class LogoutAction {

  @Intercept(LogoutInterceptor.class)
  @Action
  public void logout() {

  }
}
