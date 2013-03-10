package pl.bristleback.server.bristle.security.authorisation.conditions;

import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;

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
  public static <T extends IdentifiedUser> SendCondition<T> getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isApplicable(IdentifiedUser user) {
    return true;
  }
}
