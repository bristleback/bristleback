package pl.bristleback.server.bristle.engine.tcp;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.annotations.ConfigurationElement;
import pl.bristleback.server.bristle.engine.base.AbstractServerEngine;

import java.net.ServerSocket;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-09 16:02:59 <br/>
 *
 * @author Wojciech Niemiec
 */
@ConfigurationElement("java.io")
public class JavaIOServerEngine extends AbstractServerEngine {
  private static Logger log = Logger.getLogger(JavaIOServerEngine.class.getName());

  private ServerSocket serverSocket;

  public void startServer() {

  }

  public void stopServer() {
  }

  @Override
  public void onConnectionOpened(WebsocketConnector connector) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void onConnectionClose(WebsocketConnector connector) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void sendPacket(WebsocketConnector connector, String contentAsString) throws Exception {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void sendPacket(WebsocketConnector connector, byte[] contentAsBytes) throws Exception {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
