package pl.bristleback.server.bristle.engine.base;

import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;

import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-18 10:00:25 <br/>
 *
 * @author Wojciech Niemiec
 */
public abstract class AbstractConnector implements WebsocketConnector {

  private String connectorId;
  private ServerEngine engine;
  private DataController dataController;
  private String websocketVersion;
  private Map<String, Object> variables;

  public AbstractConnector(ServerEngine engine, DataController controller) {
    this.engine = engine;
    connectorId = engine.getEngineConfiguration().getNextConnectorId() + "";
    dataController = controller;
    variables = new HashMap<String, Object>();
  }

  public String getConnectorId() {
    return connectorId;
  }

  @Override
  public ServerEngine getEngine() {
    return engine;
  }

  public DataController getDataController() {
    return dataController;
  }

  public String getWebsocketVersion() {
    return websocketVersion;
  }

  public void setWebsocketVersion(String websocketVersion) {
    this.websocketVersion = websocketVersion;
  }

  public Map<String, Object> getVariables() {
    return variables;
  }

  @Override
  public Object getVariable(String variableName) {
    return variables.get(variableName);
  }

  @Override
  public void putVariable(String variableName, Object variableValue) {
    variables.put(variableName, variableValue);
  }
}