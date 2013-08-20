package pl.bristleback.server.bristle.conf.resolver.plain;

import pl.bristleback.server.bristle.conf.resolver.message.ObjectSenderInjector;
import pl.bristleback.server.bristle.conf.resolver.message.RegisteredObjectSenders;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

public class PlainObjectSenderInjector extends ObjectSenderInjector {

  public PlainObjectSenderInjector(RegisteredObjectSenders registeredObjectSenders) {
    super(registeredObjectSenders);
  }

  @Override
  protected ConditionObjectSender getObjectSenderInstance() {
    return new ConditionObjectSender();
  }
}
