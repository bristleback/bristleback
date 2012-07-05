package pl.bristleback.server.bristle.engine.base.users;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;

import java.util.UUID;

/**
 * User object created if implementation of {@link pl.bristleback.server.bristle.api.users.UserFactory} does not exist
 * <p/>
 * Created on: 2012-06-03 16:13:41 <br/>
 *
 * @author Pawel Machowski
 */
public class DefaultUser implements IdentifiedUser {

  private String id;

  public DefaultUser() {
    this.id = UUID.randomUUID().toString();
  }

  @Override
  public String getId() {
    return id;
  }
}
