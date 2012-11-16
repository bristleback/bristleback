package pl.bristleback.server.bristle.engine.tomcat.servlet;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.engine.tomcat.TomcatConnector;

import javax.servlet.http.HttpServletRequest;

public class TomcatWebsocketServletHolder extends WebSocketServlet {

  private TomcatServletWebsocketEngine engine;

  public TomcatWebsocketServletHolder(TomcatServletWebsocketEngine engine) {
    this.engine = engine;
  }

  @Override
  protected StreamInbound createWebSocketInbound(String protocol, HttpServletRequest servletRequest) {
    DataController controller = engine.getConfiguration().getDataController(protocol);
    return new TomcatConnector(engine, controller, engine.getFrontController());
  }
}
