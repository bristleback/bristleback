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

import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * Basic filter interface, used to determine message recipients.
 * In most scenarios, application creators should define custom implementations of this interface,
 * however Bristleback provides few built in filters, mainly in
 * {@link pl.bristleback.server.bristle.security.authorisation.conditions.LogicalConditions} class.
 *
 * @author Pawel Machowski
 *         created at 01.05.12 14:12
 * @see pl.bristleback.server.bristle.security.authorisation.conditions.LogicalConditions Logical filters
 * @see pl.bristleback.server.bristle.security.authorisation.conditions.AllUsersCondition All users filter
 */
public interface SendCondition<T extends UserContext> {

  /**
   * Checks if message should be sent to given user
   * Used in message senders
   *
   * @param userContext The examined user.
   * @return true if message should be sent, false otherwise.
   */
  boolean isApplicable(T userContext);
}
