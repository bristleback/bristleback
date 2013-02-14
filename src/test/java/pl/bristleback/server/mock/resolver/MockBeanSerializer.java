package pl.bristleback.server.mock.resolver;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;
import pl.bristleback.server.bristle.serialization.system.json.extractor.ValueSerializer;
import pl.bristleback.server.mock.beans.MockBean;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-08 19:08:48 <br/>
 *
 * @author Wojciech Niemiec
 */
public class MockBeanSerializer implements ValueSerializer<MockBean> {

  private static Logger log = Logger.getLogger(MockBeanSerializer.class.getName());

  @Override
  public void init(BristlebackConfig configuration) {

  }

  @Override
  public MockBean toValue(JsonTokenizer tokenizer, PropertySerialization information) throws Exception {
    return null;
  }

  @Override
  public String toText(MockBean value, PropertySerialization information) {
    return value.toString();
  }
}