package pl.bristleback.server.bristle.security.authorisation.user;

import pl.bristleback.server.bristle.api.users.UserContext;

/**
 * Pawel Machowski
 * created at 01.05.12 14:35
 */
public class MockUser implements UserContext {

  private String id;

  private String name;

  public MockUser(String name) {
    this.name = name;
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }
}
