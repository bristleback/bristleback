package pl.bristleback.server.bristle.action.streaming;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionsContainer;
import pl.bristleback.server.bristle.action.exception.StreamInitializationException;
import pl.bristleback.server.bristle.api.action.StreamingActionClass;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.users.UserContext;

import javax.inject.Inject;

@ActionClass
@Component
public class BristlebackStreamingAction {

    @Inject
    private RegisteredClientStreams registeredClientStreams;

    @Inject
    private ActionsContainer actionsContainer;

    @Action
    public boolean initStreaming(String handlingStreamingClass, UserContext userContext) {
        if (registeredClientStreams.isUserStreamExist(userContext)) {
            throw new StreamInitializationException("Could not init streaming for " + handlingStreamingClass + " action class. " +
                    "User with id " + userContext.getId() + " has already registered stream.");
        }
        ActionClassInformation actionClassInformation = actionsContainer.getActionClass(handlingStreamingClass);
        if (!StreamingActionClass.class.isAssignableFrom(actionClassInformation.getType())) {
            throw new StreamInitializationException("Action class " + actionClassInformation.getName() + " is not a streaming class. \n" +
                    "Streaming action class must implement " + StreamingActionClass.class.getSimpleName() + " interface.");
        }

        registeredClientStreams.registerUserStream(userContext, actionClassInformation);

        return true;
    }
}
