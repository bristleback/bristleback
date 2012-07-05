package pl.bristleback.server.bristle.api.users;

/**
 * Pawel Machowski
 * created at 01.05.12 14:00
 */
public interface IdentifiedUser {

  /**
   * must return unique id number for each connected user
   *
   * @return
   */
  String getId();
}
