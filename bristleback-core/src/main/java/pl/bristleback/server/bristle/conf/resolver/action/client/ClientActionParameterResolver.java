package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.client.ClientActionParameterInformation;
import pl.bristleback.server.bristle.api.annotations.Ignore;
import pl.bristleback.server.bristle.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-21 10:08:07 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ClientActionParameterResolver {

  public ClientActionParameterInformation prepareActionParameter(Type parameterType, Annotation[] annotations) {
    Ignore ignoreSerialization = ReflectionUtils.findAnnotation(annotations, Ignore.class);
    boolean forSerialization = ignoreSerialization == null;
    return new ClientActionParameterInformation(parameterType, forSerialization);
  }
}
