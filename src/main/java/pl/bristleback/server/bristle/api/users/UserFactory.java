package pl.bristleback.server.bristle.api.users;

/**
 * Factory interface used by bristleback framework to create new instance of user. When new WebSocket connection is
 * opened it creates new instance of {@link IdentifiedUser} If you want to use your own implementation of user object
 * you should implement this interface and specify created class in configuration. If there is no implementation of
 * UserFactory, {@link pl.bristleback.server.bristle.engine.base.users.DefaultUserFactory} will be used by framework.
 *
 * @author Pawel Machowski created at 03.06.12 16:34
 */
public interface UserFactory<T extends IdentifiedUser> {

  /**
   * Creates new instance of user object. For every new WebSocket connection it should return unique instance of  {@link
   * IdentifiedUser} implementation.
   *
   * @return new instance of user object.
   */
  T createNewUser();

}
