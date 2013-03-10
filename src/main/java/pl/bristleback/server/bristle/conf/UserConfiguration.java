package pl.bristleback.server.bristle.conf;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.api.users.UserFactory;
import pl.bristleback.server.bristle.security.UsersContainer;

/**
 * //@todo class description
 * <p/>
 * Created on: 17.02.13 14:02 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserConfiguration {

  private Class<? extends IdentifiedUser> userClass;

  private UserFactory userFactory;

  private UsersContainer usersContainer;


  public UserConfiguration(UserFactory userFactory, Class<? extends IdentifiedUser> userClass, UsersContainer usersContainer) {
    this.userFactory = userFactory;
    this.userClass = userClass;
    this.usersContainer = usersContainer;
  }

  public Class<? extends IdentifiedUser> getUserClass() {
    return userClass;
  }

  public UserFactory getUserFactory() {
    return userFactory;
  }

  public UsersContainer getUsersContainer() {
    return usersContainer;
  }
}
