package pl.bristleback.server.bristle.engine.tomcat;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.OperationCodes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Tomcat 7.0.32+ WebSocketConnector
 * <p/>
 * ... Since it must extend MessageInbound, AbstractConnector code it's cloned
 * here ...
 * <p/>
 * Created on: 2012-06-17 10:14:44 <br/>
 *
 * @author Andrea Nanni
 */
public class TomcatConnector extends MessageInbound implements WebsocketConnector {
  private static Logger log = Logger.getLogger(TomcatConnector.class.getName());

  private WsOutbound connection;
  private FrontController frontController;


  public TomcatConnector(ServerEngine engine, DataController controller, FrontController frontController) {
    this.engine = engine;
    connectorId = engine.getEngineConfiguration().getNextConnectorId() + "";
    dataController = controller;
    variables = new HashMap<String, Object>();
    this.frontController = frontController;

    setByteBufferMaxSize(engine.getEngineConfiguration().getMaxBufferSize());
    setCharBufferMaxSize(engine.getEngineConfiguration().getMaxBufferSize());
  }

  public void stop() {
    ByteBuffer bb = null;
    try {
      connection.close(0, bb);
      getEngine().onConnectionClose(this);
    } catch (IOException e) {
      log.debug("Exception while closing tomcat connector", e);
    }
  }

  @Override
  protected void onOpen(WsOutbound outbound) {
    getEngine().onConnectionOpened(this);
    this.connection = outbound;
  }

  @Override
  protected void onClose(int status) {
    getEngine().onConnectionClose(this);
  }

  public WsOutbound getConnection() {
    return connection;
  }

  @Override
  protected void onBinaryMessage(ByteBuffer data) throws IOException {
    frontController.processCommand(this, OperationCodes.BINARY_FRAME_CODE.getCode(), data.array());
  }

  @Override
  protected void onTextMessage(CharBuffer data) throws IOException {
    frontController.processCommand(this, OperationCodes.TEXT_FRAME_CODE.getCode(), new String(data.array()));
  }

  @Override
  public int getReadTimeout() {
    return engine.getEngineConfiguration().getTimeout();
  }

  private String connectorId;
  private ServerEngine engine;
  private DataController dataController;
  private String websocketVersion;
  private Map<String, Object> variables;

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
