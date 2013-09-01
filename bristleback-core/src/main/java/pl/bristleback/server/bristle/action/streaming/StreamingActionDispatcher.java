package pl.bristleback.server.bristle.action.streaming;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionsContainer;
import pl.bristleback.server.bristle.api.action.StreamingActionClass;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;

import javax.inject.Inject;

@Component
public class StreamingActionDispatcher {

    @Inject
    private RegisteredClientStreams registeredClientStreams;

    @Inject
    private BristleSpringIntegration bristleSpringIntegration;

    @Inject
    private ActionsContainer actionsContainer;


    @SuppressWarnings("unchecked")
    public void dispatch(byte[] binaryData, UserContext userContext) {
        ActionClassInformation actionClassInformation = registeredClientStreams.getStreamingActionClass(userContext);
        StreamingActionClass streaming = (StreamingActionClass) actionsContainer
                .getActionClassInstance(actionClassInformation, bristleSpringIntegration);

        streaming.streamReceived(binaryData, userContext);
    }
}
