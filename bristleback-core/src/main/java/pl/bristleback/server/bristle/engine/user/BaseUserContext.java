package pl.bristleback.server.bristle.engine.user;

import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * This is a very basic implementation of {@link UserContext} interface,
 * simply providing <code>id</code> field with both getter and setter methods.
 * If there is not custom implementation of user context, this one is used.
 * <p/>
 * Created on: 23.03.13 08:31 <br/>
 *
 * @author Wojciech Niemiec
 * @see DefaultUserContextFactory
 */
public class BaseUserContext implements UserContext {

  private String id;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

}
