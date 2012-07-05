package pl.bristleback.server.bristle.listener;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.WebsocketConnector;

import java.util.Iterator;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-20 18:00:20 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ConnectionStateListenerChain {

  private static Logger log = Logger.getLogger(ConnectionStateListenerChain.class.getName());

  private List<ConnectionStateListener> listeners;

  private boolean processListeners;

  public ConnectionStateListenerChain(List<ConnectionStateListener> listeners) {
    this.listeners = listeners;
  }

  public void connectorStarted(WebsocketConnector connector) {
    processListeners = true;
    Iterator<ConnectionStateListener> it = listeners.iterator();
    while (processListeners && it.hasNext()) {
      it.next().connectorStarted(connector);
    }
  }

  public void connectorStopped(WebsocketConnector connector) {
    processListeners = true;
    Iterator<ConnectionStateListener> it = listeners.iterator();
    while (processListeners && it.hasNext()) {
      it.next().connectorStopped(connector);
    }
  }

  /**
   * Cancels listeners processing, any next remaining listeners won't be processed.
   */
  public void cancelChain() {
    processListeners = false;
  }
}
