package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * Pawel Machowski
 * created at 01.05.12 14:12
 */
public interface SendCondition<T extends IdentifiedUser> {

  /**
   * Checks if message should be sent to given user
   * Used in message senders
   */
  boolean isApplicable(T user);
}
