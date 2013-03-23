package pl.bristleback.server.bristle.engine.user;

import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * //@todo class description
 * <p/>
 * Created on: 23.03.13 08:31 <br/>
 *
 * @author Wojciech Niemiec
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
