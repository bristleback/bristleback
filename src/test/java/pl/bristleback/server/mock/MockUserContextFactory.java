package pl.bristleback.server.mock;

import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.api.users.UserContextFactory;

/**
 * Pawel Machowski
 * created at 03.06.12 15:10
 */
public class MockUserContextFactory implements UserContextFactory<MockUserContextFactory.MockUserContext> {

  @Override
  public MockUserContext createNewUserContext() {
    return new MockUserContext();
  }

  public static class MockUserContext implements UserContext {

    public static final String MOCK_USER_ID = "mockUserId";

    @Override
    public String getId() {
      return MOCK_USER_ID;
    }

    @Override
    public void setId(String id) {

    }
  }
}


