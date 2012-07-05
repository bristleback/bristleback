package pl.bristleback.server.bristle.engine.jwebsocket;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.annotations.ConfigurationElement;
import pl.bristleback.server.bristle.engine.base.AbstractServerEngine;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-09 16:13:06 <br/>
 *
 * @author Wojciech Niemiec
 */
@ConfigurationElement("jwebsocket")
public class JwebsocketServerEngine extends AbstractServerEngine {
  private static Logger log = Logger.getLogger(JwebsocketServerEngine.class.getName());

  public static final String JWEBSOCKET_FILE_PROPERTY = "engine.jwebsocket.configuration.location";
  public static final String DEFAULT_JWEBSOCKET_FILE_LOCATION = "conf/jwebsocket.xml";

  public void startServer() {
    String jwebsocketXmlLocation = getJwebsocketXmlFileLocation();
    jwebsocketXmlLocation.charAt(0);
  }

  private String getJwebsocketXmlFileLocation() {
    String jwebsocketXmlLocation = getEngineConfiguration().getProperties().get(JWEBSOCKET_FILE_PROPERTY);
    if (StringUtils.isBlank(jwebsocketXmlLocation)) {
      jwebsocketXmlLocation = DEFAULT_JWEBSOCKET_FILE_LOCATION;
    }
    return jwebsocketXmlLocation;
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
  public void sendPacket(WebsocketConnector connector, String contentAsString) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void sendPacket(WebsocketConnector connector, byte[] contentAsBytes) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
