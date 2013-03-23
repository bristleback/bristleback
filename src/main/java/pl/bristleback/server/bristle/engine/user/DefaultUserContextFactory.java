package pl.bristleback.server.bristle.engine.user;

import pl.bristleback.server.bristle.api.users.UserContextFactory;

/**
 * Provides user object for framework
 * If implementation of {@link pl.bristleback.server.bristle.api.users.UserContextFactory} is not provided by framework user,
 * that class is used and {@link BaseUserContext} object is created.
 * <p/>
 *
 * @author Pawel Machowski
 *         created at 03.05.12 16:34
 */
public class DefaultUserContextFactory implements UserContextFactory<BaseUserContext> {

  @Override
  public BaseUserContext createNewUserContext() {
    return new BaseUserContext();
  }

}
