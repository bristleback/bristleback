package pl.bristleback.server.bristle.conf.resolver.message;

import pl.bristleback.server.bristle.message.ConditionObjectSender;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisteredObjectSenders {

  public static final String COMPONENT_NAME = "system.sender.container";

  private Map<Field, ConditionObjectSender> registeredSenders = new HashMap<Field, ConditionObjectSender>();

  public boolean containsSenderForField(Field field) {
    return registeredSenders.containsKey(field);
  }

  public ConditionObjectSender getSender(Field field) {
    return registeredSenders.get(field);
  }

  public void putSender(Field field, ConditionObjectSender conditionObjectSender) {
    registeredSenders.put(field, conditionObjectSender);
  }

  public List<ConditionObjectSender> getRegisteredSenders() {
    return new ArrayList<ConditionObjectSender>(registeredSenders.values());
  }
}
