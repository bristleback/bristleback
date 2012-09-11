package pl.bristleback.server.bristle.action.extractor;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-08-09 14:43:49 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class IdentifiedUserExtractor implements ActionParameterExtractor<IdentifiedUser> {

  @Override
  public IdentifiedUser fromTextContent(String text, ActionParameterInformation parameterInformation, ActionExecutionContext context) throws Exception {
    return context.getUser();
  }

  @Override
  public boolean isDeserializationRequired() {
    return false;
  }

  @Override
  public void init(BristlebackConfig configuration) {

  }
}
