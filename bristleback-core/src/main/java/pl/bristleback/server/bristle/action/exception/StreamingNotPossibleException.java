package pl.bristleback.server.bristle.action.exception;

import pl.bristleback.server.bristle.BristleRuntimeException;

public class StreamingNotPossibleException extends BristleRuntimeException {

    public StreamingNotPossibleException() {
        super("User has not registered stream.");
    }
}
