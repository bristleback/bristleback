package pl.bristleback.server.bristle.action.exception.handler;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;
import pl.bristleback.server.bristle.authorisation.user.UsersContainer;

import javax.inject.Inject;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-03-10 23:39:22 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class MessageDeserializationExceptionHandler implements ActionExceptionHandler<Exception> {
  private static Logger log = Logger.getLogger(MessageDeserializationExceptionHandler.class.getName());

  private ServerEngine serverEngine;

  @Inject
  private UsersContainer connectedUsers;

  @Override
  public void init(BristlebackConfig configuration) {
    serverEngine = configuration.getServerEngine();
  }

  @Override
  public Object handleException(Exception e, ActionExecutionContext context) {
    log.error("exception while creating ActionMessage object, connection will be closed", e);
    serverEngine.onConnectionClose(connectedUsers.getConnectorByUser(context.getUser()));
    return null;
  }

  @Override
  public ActionExecutionStage[] getHandledStages() {
    return new ActionExecutionStage[]{ActionExecutionStage.MESSAGE_DESERIALIZATION};
  }
}
