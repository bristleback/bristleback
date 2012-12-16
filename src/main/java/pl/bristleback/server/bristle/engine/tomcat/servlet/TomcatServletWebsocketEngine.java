package pl.bristleback.server.bristle.engine.tomcat.servlet;

import org.apache.catalina.connector.RequestFacade;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.api.ServletServerEngine;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.base.AbstractServerEngine;
import pl.bristleback.server.bristle.engine.tomcat.TomcatConnector;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Tomcat 7.0.32+ WebSocket ServerEngine
 * <p/>
 * Created on: 2012-06-17 10:14:44 <br/>
 *
 * @author Andrea Nanni
 */
@Component(TomcatServletWebsocketEngine.ENGINE_NAME)
public class TomcatServletWebsocketEngine extends AbstractServerEngine implements ServerEngine, ServletServerEngine {
  private static Logger log = Logger.getLogger(TomcatServletWebsocketEngine.class.getName());

  public static final String ENGINE_NAME = "system.engine.tomcat.servlet";

  @Inject
  @Named("defaultFrontController")
  private FrontController frontController;

  private TomcatWebsocketServletHolder servletHolder;

  public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpServletRequest hsr = request;
    while (hsr instanceof HttpServletRequestWrapper) {
      hsr = (HttpServletRequest) ((HttpServletRequestWrapper) hsr).getRequest();
    }
    RequestFacade facade = (RequestFacade) hsr;

    servletHolder.service(facade, response);
  }

  @Override
  public void startServer() {
    log.info("Bristleback Tomcat servlet engine started.");
    servletHolder = new TomcatWebsocketServletHolder(this);
    try {
      servletHolder.init();
    } catch (ServletException e) {
      throw new BristleInitializationException("Exception while initializing Tomcat Servlet Engine", e);
    }

  }

  @Override
  public void stopServer() {
    log.info("Bristleback Tomcat servlet engine stopped.");
  }

  @Override
  public void sendMessage(WebsocketConnector connector, String contentAsString) throws Exception {
    CharBuffer buffer = CharBuffer.wrap(contentAsString);
    ((TomcatConnector) connector).getConnection().writeTextMessage(buffer);
  }

  @Override
  public void sendMessage(WebsocketConnector connector, byte[] contentAsBytes) throws Exception {
    ByteBuffer buffer = ByteBuffer.wrap(contentAsBytes);
    ((TomcatConnector) connector).getConnection().writeBinaryMessage(buffer);
  }

  public FrontController getFrontController() {
    return frontController;
  }
}
