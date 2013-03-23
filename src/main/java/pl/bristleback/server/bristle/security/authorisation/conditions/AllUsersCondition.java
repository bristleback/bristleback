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
