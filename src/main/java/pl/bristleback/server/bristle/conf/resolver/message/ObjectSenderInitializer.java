package pl.bristleback.server.bristle.conf.resolver.message;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.security.UsersContainer;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This class simply initializes given object sender, by passing them configuration and serialization bundle.
 * {@link pl.bristleback.server.bristle.api.SerializationResolver} implementation is used to resolve serialization bundle.
 * <p/>
 * Created on: 2012-06-23 14:31:29 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ObjectSenderInitializer {

  @Inject
  @Named(SpringConfigurationResolver.CONFIG_BEAN_NAME)
  private BristlebackConfig configuration;

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Inject
  private UsersContainer connectedUsers;

  public void initObjectSender(ConditionObjectSender objectSender) {
    objectSender.init(configuration, connectedUsers);
    resolveSerializations(objectSender);
  }

  private void resolveSerializations(ConditionObjectSender objectSender) {
    SerializationBundle serializationBundle = serializationEngine.getSerializationResolver().initSerializationBundle(objectSender.getField());
    objectSender.setLocalSerializations(serializationBundle);
  }
}
