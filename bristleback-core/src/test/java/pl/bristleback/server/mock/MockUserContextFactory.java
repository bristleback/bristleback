package pl.bristleback.server.mock;

import pl.bristleback.server.bristle.api.users.UserContextFactory;

/**
 * Pawel Machowski
 * created at 03.06.12 15:10
 */
public class MockUserContextFactory implements UserContextFactory<MockUserContext> {

  @Override
  public MockUserContext createNewUserContext() {
    return new MockUserContext();
  }

}


