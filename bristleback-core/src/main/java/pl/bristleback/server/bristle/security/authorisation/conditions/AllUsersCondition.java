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

package pl.bristleback.server.bristle.security.authorisation.conditions;

import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * Utility condition, accepting every user as potential recipient.
 * <p/>
 * created at 01.05.12 14:12
 *
 * @author Pawel Machowski
 */
public final class AllUsersCondition implements SendCondition {

  private static final AllUsersCondition INSTANCE = new AllUsersCondition();

  private AllUsersCondition() {
  }

  @SuppressWarnings("unchecked")
  public static <T extends UserContext> SendCondition<T> getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isApplicable(UserContext user) {
    return true;
  }
}
