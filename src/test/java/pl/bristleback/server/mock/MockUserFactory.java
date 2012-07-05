package pl.bristleback.server.mock;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.api.users.UserFactory;

/**
 * Pawel Machowski
 * created at 03.06.12 15:10
 */
public class MockUserFactory implements UserFactory {

  @Override
  public IdentifiedUser createNewUser() {
    return new MockUser();
  }

  public static class MockUser implements IdentifiedUser {

    public static final String MOCK_USER_ID = "mockUserId";

    @Override
    public String getId() {
      return MOCK_USER_ID;
    }
  }
}


