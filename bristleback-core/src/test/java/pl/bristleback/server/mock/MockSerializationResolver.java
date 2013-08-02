package pl.bristleback.server.mock;

import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class MockSerializationResolver implements SerializationResolver<Object> {

  @Override
  public SerializationBundle initSerializationBundle(Field objectSenderField) {
    return null;
  }

  @Override
  public Object resolveSerialization(Type objectType, Annotation... annotations) {
    return "testSerializationInformation";
  }
}
