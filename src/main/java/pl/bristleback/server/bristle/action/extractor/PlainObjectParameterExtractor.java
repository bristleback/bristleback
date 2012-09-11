package pl.bristleback.server.bristle.action.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 09:29:08 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class PlainObjectParameterExtractor implements ActionParameterExtractor {

  @Inject
  @Named("serializationEngine")
  private SerializationEngine serializationEngine;

  @Override
  @SuppressWarnings("unchecked")
  public Object fromTextContent(String text, ActionParameterInformation parameterInformation, ActionExecutionContext context) throws Exception {
    return serializationEngine.deserialize(text, parameterInformation.getPropertySerialization());
  }

  @Override
  public boolean isDeserializationRequired() {
    return true;
  }

  public void init(BristlebackConfig configuration) {
  }
}
