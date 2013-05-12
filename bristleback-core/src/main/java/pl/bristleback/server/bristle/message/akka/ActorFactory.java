package pl.bristleback.server.bristle.message.akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import pl.bristleback.server.bristle.api.ServerEngine;

/**
 * <p/>
 * Created on: 01.05.13 12:56 <br/>
 *
 * @author Pawel Machowski
 */
public final class ActorFactory {

  private ActorFactory() {
    throw new UnsupportedOperationException("Static factory class");
  }

  public static Props defaultSendMessageActor(final ServerEngine server) {
    return new Props(new UntypedActorFactory() {
      public UntypedActor create() {
        return new SendMessageActor(server);
      }
    });
  }

}
