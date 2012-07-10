package pl.bristleback.server.bristle.conf.resolver.message;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.annotations.AnnotatedObjectSender;
import pl.bristleback.server.bristle.api.annotations.Serialize;
import pl.bristleback.server.bristle.authorisation.user.UsersContainer;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.conf.resolver.serialization.SerializationAnnotationResolver;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-23 14:31:29 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ConditionObjectInitializer {

  @Inject
  @Named(SpringConfigurationResolver.CONFIG_BEAN_NAME)
  private BristlebackConfig configuration;

  @Inject
  private SerializationAnnotationResolver serializationResolver;

  @Inject
  private UsersContainer connectedUsers;

  public void initObjectSender(ConditionObjectSender objectSender) {
    objectSender.init(configuration, connectedUsers);
    resolveSerializations(objectSender.getSerializationBundle());
  }

  private void resolveSerializations(SerializationBundle serializationBundle) {
    AnnotatedObjectSender senderAnnotation = serializationBundle.getField().getAnnotation(AnnotatedObjectSender.class);
    for (Serialize serialize : senderAnnotation.serialize()) {
      Object serialization = resolveSerialization(serialize);
      if (StringUtils.isEmpty(serialize.targetName())) {
        serializationBundle.addDefaultSerialization(serialization);
      } else {
        serializationBundle.addSerialization(serialize.targetName(), serialization);
      }
    }
  }

  private Object resolveSerialization(Serialize serialize) {
    return serializationResolver.resolveSerialization(serialize.target(), serialize);
  }
}
