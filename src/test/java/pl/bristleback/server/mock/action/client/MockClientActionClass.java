package pl.bristleback.server.mock.action.client;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.annotations.Bind;
import pl.bristleback.server.bristle.api.annotations.ClientAction;
import pl.bristleback.server.bristle.api.annotations.ClientActionClass;
import pl.bristleback.server.bristle.api.annotations.Ignore;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-21 17:47:21 <br/>
 *
 * @author Wojciech Niemiec
 */
@ClientActionClass
public class MockClientActionClass {

  public static final String SIMPLE_ACTION_NAME = "simpleClientAction";

  public static final String MULTIPLE_PARAMS_ONE_SERIALIZED_ACTION_NAME = "multipleParamsOneSerialized";

  public static final String SINGLE_PARAM_ACTION_METHOD_NAME = "singleParam";
  public static final String SINGLE_PARAM_ACTION_NAME = "nonDefaultName";

  @ClientAction
  public IdentifiedUser simpleClientAction(String param1, @Bind() VerySimpleMockBean simpleMockBean, @Ignore IdentifiedUser ignoredParam) {
    return ignoredParam;
  }

  @ClientAction()
  public IdentifiedUser multipleParamsOneSerialized(@Ignore String param1, String param2, @Ignore IdentifiedUser ignoredParam) {
    return ignoredParam;
  }

  @ClientAction("nonDefaultName")
  public IdentifiedUser singleParam(IdentifiedUser identifiedUser) {
    return identifiedUser;
  }
}
