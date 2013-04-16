package pl.bristleback.server.mock.resolver;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.mock.beans.NonDefaultSerializedMockBean;

@Component
public class SimpleMockParameterExtractor implements ActionParameterExtractor<NonDefaultSerializedMockBean> {

  private static Logger log = Logger.getLogger(SimpleMockParameterExtractor.class.getName());

  @Override
  public NonDefaultSerializedMockBean fromTextContent(String text, ActionParameterInformation parameterInformation, ActionExecutionContext context) throws Exception {
    NonDefaultSerializedMockBean bean = new NonDefaultSerializedMockBean();
    bean.setProperty1(3);
    bean.setProperty2(text);
    return bean;
  }

  @Override
  public boolean isDeserializationRequired() {
    return true;
  }

  @Override
  public void init(BristlebackConfig configuration) {
  }
}
