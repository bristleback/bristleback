package pl.bristleback.server.mock;

import pl.bristleback.server.bristle.api.users.UserContext;

public class MockUserContext implements UserContext {

    public static final String MOCK_USER_ID = "mockUserId";

    @Override
    public String getId() {
      return MOCK_USER_ID;
    }

    @Override
    public void setId(String id) {

    }
  }
