package pl.bristleback.server.bristle.listener;

import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.users.UserContext;

import java.util.Iterator;
import java.util.List;

/**
 * Objects of this class are created whenever new connection is established or connection is closed.
 * It iterates over all connection state listeners found at server start.
 * Every listener can cancel whole listeners execution chain using {@link ConnectionStateListenerChain#cancelChain()} method.
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
  public void connectorStarted(UserContext user) {
    processListeners = true;
    Iterator<ConnectionStateListener> it = listeners.iterator();
    while (processListeners && it.hasNext()) {
      ConnectionStateListener listener = it.next();
      listener.userConnected(user, this);
    }
  }

  @SuppressWarnings("unchecked")
  public void connectorStopped(UserContext user) {
    processListeners = true;
    Iterator<ConnectionStateListener> it = listeners.iterator();
    while (processListeners && it.hasNext()) {
      it.next().userDisconnected(user, this);
    }
  }

  /**
   * Cancels listeners processing, any next remaining listeners won't be processed.
   */
  public void cancelChain() {
    processListeners = false;
  }
}
