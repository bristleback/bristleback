package pl.bristleback.server.bristle.api;

import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-14 16:39:33 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface WebsocketConnector<T> {

  DataController getDataController();

  void setDataController(DataController controller);

  String getConnectorId();

  void setConnectorId(String connectorId);

  void stop();

  ServerEngine getEngine();

  String getWebsocketVersion();

  void setWebsocketVersion(String version);

  Map<String, Object> getVariables();

  //TODO machu

  boolean isAuthorisedToHandleMessage(WebsocketMessage message);

  Object getVariable(String variableName);

  void putVariable(String variableName, Object variableValue);
}