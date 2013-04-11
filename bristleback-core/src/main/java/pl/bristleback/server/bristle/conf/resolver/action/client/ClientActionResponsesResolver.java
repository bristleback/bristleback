package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.action.ClientActionSender;
import pl.bristleback.server.bristle.integration.spring.BristleSpringIntegration;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-07 21:13:32 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionResponsesResolver {

  @Inject
  @Named("springIntegration")
  private BristleSpringIntegration springIntegration;

  public Map<Class, ClientActionSender> resolve() {
    Map<Class, ClientActionSender> strategies = new HashMap<Class, ClientActionSender>();

    Map<String, ClientActionSender> strategiesBeans = springIntegration.getBeansOfType(ClientActionSender.class);
    for (ClientActionSender strategy : strategiesBeans.values()) {
      Type type = ReflectionUtils.getParameterTypes(strategy.getClass(), ClientActionSender.class)[0];
      Class objectType;
      if (type instanceof ParameterizedType) {
        objectType = (Class) ((ParameterizedType) type).getRawType();
      } else {
        objectType = (Class) type;
      }

      strategies.put(objectType, strategy);
    }

    return strategies;
  }
}
