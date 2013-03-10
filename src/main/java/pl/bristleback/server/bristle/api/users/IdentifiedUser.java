package pl.bristleback.server.bristle.api.users;

/**
 * Interface representing user connected to BristleBack server. Provides user friendly abstraction for dealing with low
 * level WebSocket mechanism.
 * <p/>
 * For each WebSocket connection new instance of IdentifiedUser is created. All instances off currently connected users
 * are held in {@link pl.bristleback.server.bristle.security.UsersContainer}. Framework user can create own
 * implementation of this interface and create new {@link UserFactory} to handle application specific user behaviour.
 * <p/>
 * If there is no implementation of IdentifiedUser, instance of class {@link pl.bristleback.server.bristle.engine.base.users.DefaultUser}
 * will be created.
 *
 * @author Pawel Machowski created at 01.05.12 14:00
 */
public interface IdentifiedUser {

  /**
   * Must return unique id number for each connected user
   *
   * @return id number of connected user.
   */
  String getId();
}
