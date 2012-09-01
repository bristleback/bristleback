package pl.bristleback.server.bristle.api;

import java.util.Map;

/**
 * Websocket connector is an abstract representation of single client connection.
 * Object implementing this interface has physical implementation of network socket as well as other information,
 * like used sub protocol (in Bristleback it's one of the {@link pl.bristleback.server.bristle.api.DataController DataController} interface implementation)
 * or connection id number.
 * <p/>
 * Created on: 2011-07-14 16:39:33 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface WebsocketConnector<T> {

  /**
   * Gets {@link pl.bristleback.server.bristle.api.DataController DataController} used by client represented by this connector.
   * Data controller (sub protocol, according to WebSockets specification) is chosen by client when connecting.
   *
   * @return data controller used in connection.
   */
  DataController getDataController();

  /**
   * Gets connection ID number.
   *
   * @return connection ID number.
   */
  String getConnectorId();

  /**
   * Physically disconnects client, releasing all resources used by this connection and invoking
   * {@link pl.bristleback.server.bristle.api.ServerEngine#onConnectionClose(WebsocketConnector)} method.
   */
  void stop();

  /**
   * Gets Server Engine used.
   *
   * @return server engine.
   */
  ServerEngine getEngine();

  /**
   * Gets version of WebSockets protocol used by this connection.
   *
   * @return WebSockets version.
   */
  String getWebsocketVersion();

  /**
   * Sets version of WebSockets protocol used by this connection.
   *
   * @param version WebSockets version.
   */
  void setWebsocketVersion(String version);

  Map<String, Object> getVariables();

  Object getVariable(String variableName);

  void putVariable(String variableName, Object variableValue);
}