package pl.bristleback.server.bristle.api;

public interface SerializationEngine<T> {

  SerializationResolver<T> getSerializationResolver();

  void init(BristlebackConfig configuration);

  Object deserialize(String serializedObject, T serialization) throws Exception;

  String serialize(Object object, T serialization) throws Exception;

  String serialize(Object object) throws Exception;
}
