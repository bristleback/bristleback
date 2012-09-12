package pl.bristleback.server.bristle.engine.base.users;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.api.users.UserFactory;

/**
 * Provides user object for framework
 * If implementation of {@link UserFactory} is not provided by framework user, that class is used and {@link DefaultUser} object is created
 * <p/>
 * Pawel Machowski
 * created at 03.05.12 16:34
 */
public class DefaultUserFactory implements UserFactory {

  @Override
  public IdentifiedUser createNewUser() {
    return new DefaultUser();
  }

}
