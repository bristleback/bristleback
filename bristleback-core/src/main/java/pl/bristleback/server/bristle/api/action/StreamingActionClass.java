package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.api.users.UserContext;

public interface StreamingActionClass<U extends UserContext> {

    void streamReceived(byte[] bytes, U userContext);
}
