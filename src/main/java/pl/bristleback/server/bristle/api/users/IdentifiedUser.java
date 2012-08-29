package pl.bristleback.server.bristle.api.users;

/**
 * //TODO - class description
 *
 * @author Pawel Machowski
 *         created at 01.05.12 14:00
 */
public interface IdentifiedUser {

  /**
   * Must return unique id number for each connected user
   *
   * @return id number of connected user.
   */
  String getId();
}
