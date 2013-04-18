package pl.bristleback.server.bristle.engine.jetty;

import org.eclipse.jetty.websocket.WebSocket;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.conf.EngineConfig;
import pl.bristleback.server.bristle.engine.OperationCode;
import pl.bristleback.server.bristle.engine.base.AbstractConnector;

public class JettyConnector extends AbstractConnector implements WebSocket, WebSocket.OnTextMessage, WebSocket.OnBinaryMessage, WebSocket.OnControl {

  private Connection connection;
  private FrontController frontController;

  public JettyConnector(ServerEngine engine, DataController controller, FrontController frontController) {
    super(engine, controller);
    this.frontController = frontController;
  }

  public void onOpen(Connection newConnection) {
    EngineConfig engineConfiguration = getEngine().getEngineConfiguration();
    newConnection.setMaxBinaryMessageSize(engineConfiguration.getMaxFrameSize());
    newConnection.setMaxTextMessageSize(engineConfiguration.getMaxFrameSize());
    newConnection.setMaxIdleTime(engineConfiguration.getTimeout());
    getEngine().onConnectionOpened(this);
    this.connection = newConnection;
  }

  public void onClose(int i, String s) {
    getEngine().onConnectionClose(this);
  }

  public void stop() {
    connection.close();
  }

  public Connection getConnection() {
    return connection;
  }

  public void onMessage(String data) {
    frontController.processCommand(this, OperationCode.TEXT_FRAME_CODE.getCode(), data);
  }

  @Override
  public void onMessage(byte[] data, int offset, int length) {
    frontController.processCommand(this, OperationCode.BINARY_FRAME_CODE.getCode(), data);
  }

  @Override
  public boolean onControl(byte controlCode, byte[] data, int offset, int length) {
    frontController.processCommand(this, controlCode, data);
    return true;
  }
}
