/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.message.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.WebsocketMessage;
import pl.bristleback.server.bristle.message.AbstractMessageDispatcher;

/**
 * created at 23.09.12
 *
 * @author Pawel Machowski
 */
@Component("system.dispatcher.multi.threaded")
public class MultiThreadedMessageDispatcher extends AbstractMessageDispatcher {
  private static Logger log = Logger.getLogger(MultiThreadedMessageDispatcher.class.getName());

  private boolean dispatcherRunning;
  private ActorRef sendMessageActor;
  private ActorSystem akkaSystem;

  @Override
  public void addMessage(WebsocketMessage message) {
    if (dispatcherRunning) {
      log.debug("Sending a server message: " + message.getContent());
      for (Object connector : message.getRecipients()) {
        sendMessageActor.tell(new MessageForConnector(message, (WebsocketConnector) connector));
      }
    }
  }

  @Override
  public void dispatchMessages() throws Exception {
    //empty, message is dispatched as soon at it's sent do dispacher, Akka library handles it then.
  }

  @Override
  public void startDispatching() {
    if (dispatcherRunning) {
      throw new IllegalStateException("Dispatcher already running.");
    }
    log.info("Starting multi threaded message dispatcher");
    akkaSystem = ActorSystem.create("BristlebackSystem");
    this.sendMessageActor = akkaSystem.actorOf(ActorFactory.defaultSendMessageActor(getServer()), "MessageDispatcherActor");
    setDispatcherRunning(true);
  }

  @Override
  public void stopDispatching() {
    if (!dispatcherRunning) {
      throw new IllegalStateException("Dispatcher is not running yet");
    }
    akkaSystem.shutdown();
    setDispatcherRunning(false);
  }


  private void setDispatcherRunning(boolean dispatcherRunning) {
    this.dispatcherRunning = dispatcherRunning;
  }
}
