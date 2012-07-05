package pl.bristleback.server.bristle.authorisation.user;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;

import java.util.UUID;

/**
 * Pawel Machowski
 * created at 01.05.12 14:35
 */
public class MockUser implements IdentifiedUser {

  private String id;
  private String name;

  public MockUser(String name) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
