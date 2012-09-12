package pl.bristleback.server.bristle.action.client;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.authorisation.user.UsersContainer;
import pl.bristleback.server.bristle.conf.resolver.SpringConfigurationResolver;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-08 21:03:42 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionsInitializer {

  @Inject
  private ClientActionClasses clientActionClasses;

  @Inject
  private BristleSpringIntegration springIntegration;

  @Inject
  private UsersContainer usersContainer;

  @Inject
  @Named(SpringConfigurationResolver.CONFIG_BEAN_NAME)
  private BristlebackConfig configuration;

  public void initActionClasses() {
    ClientActionProxyInterceptor proxyInterceptor = getClientActionInterceptor();
    if (proxyInterceptor == null) {
      // client actions not enabled
      return;
    }
    SerializationBundle serializationBundle = new SerializationBundle();
    ConditionObjectSender objectSender = initObjectSender(serializationBundle);

    for (ClientActionClassInformation actionClassInformation : clientActionClasses.getActionClasses().values()) {
      for (ClientActionInformation actionInformation : actionClassInformation.getClientActions().values()) {
        serializationBundle.addSerialization(actionInformation.getFullName(), actionInformation.getSerialization());
      }
    }
    proxyInterceptor.init(clientActionClasses, objectSender);

  }

  private ClientActionProxyInterceptor getClientActionInterceptor() {
    try {
      return springIntegration.getApplicationBean(ClientActionProxyInterceptor.class);
    } catch (NoSuchBeanDefinitionException e) {
      return null;
    }
  }

  private ConditionObjectSender initObjectSender(SerializationBundle serializationBundle) {
    ConditionObjectSender objectSender = new ConditionObjectSender();
    objectSender.init(configuration, usersContainer);
    objectSender.setSerializationBundle(serializationBundle);
    return objectSender;
  }
}
