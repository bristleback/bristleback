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

  public static <T extends IdentifiedUser> SendCondition and(final SendCondition<T> first, final SendCondition<T> second) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T user) {
        return first.isApplicable(user) && second.isApplicable(user);
      }
    };
  }

  public static <T extends IdentifiedUser> SendCondition and(final SendCondition<T>... conditions) {
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

  public static <T extends IdentifiedUser> SendCondition or(final SendCondition<T> first, final SendCondition<T> second) {
    return new SendCondition<T>() {
      @Override
      public boolean isApplicable(T user) {
        return first.isApplicable(user) || second.isApplicable(user);
      }
    };
  }

  public static <T extends IdentifiedUser> SendCondition or(final SendCondition<T>... conditions) {
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
}
