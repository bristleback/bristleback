package pl.bristleback.server.bristle.action.streaming;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.exception.StreamingNotPossibleException;
import pl.bristleback.server.bristle.api.users.UserContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class RegisteredClientStreams {

  private Map<String, ActionClassInformation> registeredStreamActions = Collections.synchronizedMap(new HashMap<String, ActionClassInformation>());

  public boolean isUserStreamExist(UserContext userContext) {
    return registeredStreamActions.containsKey(userContext.getId());
  }

  public void registerUserStream(UserContext userContext, ActionClassInformation actionClass) {
    registeredStreamActions.put(userContext.getId(), actionClass);
  }

  public ActionClassInformation getStreamingActionClass(UserContext userContext) {
    if (!isUserStreamExist(userContext)) {
      throw new StreamingNotPossibleException();
    }
    return registeredStreamActions.get(userContext.getId());
  }
}
