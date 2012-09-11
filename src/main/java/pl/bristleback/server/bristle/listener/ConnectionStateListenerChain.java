package pl.bristleback.server.bristle.listener;

import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;

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

  private List<ConnectionStateListener> listeners;

  private boolean processListeners;

  public ConnectionStateListenerChain(List<ConnectionStateListener> listeners) {
    this.listeners = listeners;
  }

  @SuppressWarnings("unchecked")
  public void connectorStarted(IdentifiedUser user) {
    processListeners = true;
    Iterator<ConnectionStateListener> it = listeners.iterator();
    while (processListeners && it.hasNext()) {
      it.next().userConnected(user);
    }
  }

  @SuppressWarnings("unchecked")
  public void connectorStopped(IdentifiedUser user) {
    processListeners = true;
    Iterator<ConnectionStateListener> it = listeners.iterator();
    while (processListeners && it.hasNext()) {
      it.next().userDisconnected(user);
    }
  }

  /**
   * Cancels listeners processing, any next remaining listeners won't be processed.
   */
  public void cancelChain() {
    processListeners = false;
  }
}
