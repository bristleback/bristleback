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
 * Pack of general purpose logical conditions.
 * <p/>
 * Created on: 2012-08-26 11:47:53 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class LogicalConditions {

  private LogicalConditions() {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a new condition instance that works as the logical "AND" expression.
   *
   * @param first  first condition.
   * @param second second condition.
   * @param <T>    type of user implementation.
   * @return "AND" condition object.
   */
  public static <T extends UserContext> SendCondition<T> and(final SendCondition<T> first, final SendCondition<T> second) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T userContext) {
        return first.isApplicable(userContext) && second.isApplicable(userContext);
      }
    };
  }

  /**
   * Creates a new condition instance that works as the logical "AND" expression.
   *
   * @param conditions conditions.
   * @param <T>        type of user implementation.
   * @return "AND" condition object.
   */
  public static <T extends UserContext> SendCondition<T> and(final SendCondition<T>... conditions) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T userContext) {
        for (SendCondition<T> condition : conditions) {
          if (!condition.isApplicable(userContext)) {
            return false;
          }
        }
        return true;
      }
    };
  }

  /**
   * Creates a new condition instance that works as the logical "OR" expression.
   *
   * @param first  first condition.
   * @param second second condition.
   * @param <T>    type of user implementation.
   * @return "OR" condition object.
   */
  public static <T extends UserContext> SendCondition<T> or(final SendCondition<T> first, final SendCondition<T> second) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T userContext) {
        return first.isApplicable(userContext) || second.isApplicable(userContext);
      }
    };
  }

  /**
   * Creates a new condition instance that works as the logical "OR" expression.
   *
   * @param conditions conditions.
   * @param <T>        type of user implementation.
   * @return "OR" condition object.
   */
  public static <T extends UserContext> SendCondition<T> or(final SendCondition<T>... conditions) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T userContext) {
        for (SendCondition<T> condition : conditions) {
          if (condition.isApplicable(userContext)) {
            return true;
          }
        }
        return false;
      }
    };
  }

  /**
   * Creates a new condition instance that works as the logical "NOT" expression.
   *
   * @param condition condition to negate.
   * @param <T>       type of user implementation.
   * @return "NOT" condition object.
   */
  public static <T extends UserContext> SendCondition<T> not(final SendCondition<T> condition) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T userContext) {
        return !condition.isApplicable(userContext);
      }
    };
  }
}
