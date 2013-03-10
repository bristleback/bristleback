package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;

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
public interface SendCondition<T extends IdentifiedUser> {

  /**
   * Checks if message should be sent to given user
   * Used in message senders
   *
   * @param user The examined user.
   * @return true if message should be sent, false otherwise.
   */
  boolean isApplicable(T user);
}
