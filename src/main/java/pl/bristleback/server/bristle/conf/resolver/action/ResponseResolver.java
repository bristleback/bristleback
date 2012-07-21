package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.response.ActionResponseInformation;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.Serialize;
import pl.bristleback.server.bristle.conf.resolver.serialization.SerializationAnnotationResolver;

import javax.inject.Inject;
import java.lang.reflect.Method;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-16 12:13:12 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class ResponseResolver {

  private static Logger log = Logger.getLogger(ResponseResolver.class.getName());

  @Inject
  private SerializationAnnotationResolver serializationResolver;

  ActionResponseInformation resolveResponse(Method action) {
    Action actionAnnotation = action.getAnnotation(Action.class);
    ActionResponseInformation responseInformation = new ActionResponseInformation();

    if (action.getReturnType().equals(Void.TYPE)) {
      responseInformation.setVoidResponse(true);
    }

    resolveResponseSerialization(action, actionAnnotation, responseInformation);

    return responseInformation;
  }

  private void resolveResponseSerialization(Method action, Action actionAnnotation,
                                            ActionResponseInformation responseInformation) {
    Serialize[] serializeAnnotations = actionAnnotation.response();

    Serialize takenAnnotation = null;
    if (serializeAnnotations.length > 0) {
      takenAnnotation = serializeAnnotations[0];
      if (serializeAnnotations.length > 1) {
        log.debug("Only first serialize annotation will be used as the response serialization information");
      }
    }

    Object serialization = serializationResolver.resolveSerialization(action.getGenericReturnType(), takenAnnotation);

    responseInformation.setSerialization(serialization);
  }
}
