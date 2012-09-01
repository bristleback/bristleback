package pl.bristleback.server.bristle.authorisation.conditions;

import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * Utility condition, accepting every user as potential recipient.
 * <p/>
 * created at 01.05.12 14:12
 *
 * @author Pawel Machowski
 */
public final class AllUsersCondition implements SendCondition<IdentifiedUser> {

  private static final AllUsersCondition INSTANCE = new AllUsersCondition();

  private AllUsersCondition() {
  }

  public static AllUsersCondition getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isApplicable(IdentifiedUser user) {
    return true;
  }
}
