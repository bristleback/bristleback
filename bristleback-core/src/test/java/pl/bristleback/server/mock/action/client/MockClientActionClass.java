package pl.bristleback.server.mock.action.client;

import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.api.annotations.Ignore;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

@ClientActionClass
public class MockClientActionClass {

  public static final String SIMPLE_ACTION_NAME = "simpleClientAction";

  public static final String MULTIPLE_PARAMS_ONE_SERIALIZED_ACTION_NAME = "multipleParamsOneSerialized";

  public static final String SINGLE_PARAM_ACTION_METHOD_NAME = "singleParam";

  public static final String SINGLE_PARAM_ACTION_NAME = "nonDefaultName";

  @ClientAction
  public UserContext simpleClientAction(String param1, @Bind() VerySimpleMockBean simpleMockBean, @Ignore UserContext ignoredParam) {
    return ignoredParam;
  }

  @ClientAction()
  public UserContext multipleParamsOneSerialized(@Ignore String param1, String param2, @Ignore UserContext ignoredParam) {
    return ignoredParam;
  }

  @ClientAction("nonDefaultName")
  public UserContext singleParam(UserContext identifiedUser) {
    return identifiedUser;
  }
}
