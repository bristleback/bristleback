package pl.bristleback.server.bristle.api.users;

/**
 * Factory interface used by bristleback framework to create new instance of user context. When new WebSocket connection is
 * opened it creates new instance of {@link UserContext} If you want to use your own implementation of user context object
 * you should implement this interface and specify created class in configuration. If there is no implementation of
 * UserContextFactory, {@link pl.bristleback.server.bristle.engine.user.DefaultUserContextFactory} will be used by framework.
 *
 * @author Pawel Machowski created at 03.06.12 16:34
 */
public interface UserContextFactory<T extends UserContext> {

  /**
   * Creates new instance of user object. For every new WebSocket connection it should return unique instance of  {@link
   * UserContext} implementation.
   *
   * @return new instance of user object.
   */
  T createNewUserContext();

}
