package pl.bristleback.server.bristle.authorisation.conditions;

import pl.bristleback.server.bristle.api.action.SendCondition;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * Pawel Machowski
 * created at 01.05.12 14:12
 */
public class AllUsersCondition implements SendCondition<IdentifiedUser> {
  @Override
  public boolean isApplicable(IdentifiedUser user) {
    return true;
  }
}
