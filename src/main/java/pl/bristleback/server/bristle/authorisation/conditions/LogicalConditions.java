package pl.bristleback.server.bristle.authorisation.conditions;

import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;

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
  public static <T extends IdentifiedUser> SendCondition<T> and(final SendCondition<T> first, final SendCondition<T> second) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T user) {
        return first.isApplicable(user) && second.isApplicable(user);
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
  public static <T extends IdentifiedUser> SendCondition<T> and(final SendCondition<T>... conditions) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T user) {
        for (SendCondition<T> condition : conditions) {
          if (!condition.isApplicable(user)) {
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
  public static <T extends IdentifiedUser> SendCondition<T> or(final SendCondition<T> first, final SendCondition<T> second) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T user) {
        return first.isApplicable(user) || second.isApplicable(user);
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
  public static <T extends IdentifiedUser> SendCondition<T> or(final SendCondition<T>... conditions) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T user) {
        for (SendCondition<T> condition : conditions) {
          if (condition.isApplicable(user)) {
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
  public static <T extends IdentifiedUser> SendCondition<T> not(final SendCondition<T> condition) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T user) {
        return !condition.isApplicable(user);
      }
    };
  }
}
