package pl.bristleback.server.mock;

import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;

public class MockSerializationEngine implements SerializationEngine {

  @Override
  public SerializationResolver<Object> getSerializationResolver() {
    return new MockSerializationResolver();
  }

  @Override
  public Object deserialize(String serializedObject, Object serialization) throws Exception {
    return null;
  }

  @Override
  public String serialize(Object object, Object serialization) throws Exception {
    return null;
  }

  @Override
  public String serialize(Object object) throws Exception {
    return null;
  }

  @Override
  public void init(BristlebackConfig configuration) {

  }
}
