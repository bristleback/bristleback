package pl.bristleback.client;

import pl.bristleback.client.actions.ActionController;
import pl.bristleback.client.api.BristlebackClient;
import pl.bristleback.client.connection.ServerUrl;
import pl.bristleback.client.connection.WebSocketConnection;
import pl.bristleback.client.serialization.ToJsonSerializer;
import pl.bristleback.common.serialization.message.BristleMessage;

/**
 * <p/>
 * Created on: 5/31/13 12:46 PM <br/>
 *
 * @author Pawel Machowski
 */
public class JettyClient implements BristlebackClient {

  private ToJsonSerializer serializer = new ToJsonSerializer();

  private ActionController actionController = new ActionController();
  private WebSocketConnection connection;

  public JettyClient(String fullUrl) {
    //TODO use full url!

    connection = new WebSocketConnection(new ServerUrl(), actionController);
  }

  public void connect() {
    connection.connect();
  }

  public void disconnect() {
    connection.disconnect();
  }

  public void sendMessage(BristleMessage bristleMessage) {
    bristleMessage.withId(Math.random() + "");

    String jsonPayload = serializer.objectToJson(bristleMessage);
    connection.sendMessage(jsonPayload);

  }

}
