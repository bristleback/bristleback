Bristleback.serialization.serializationEngines = {
};

Bristleback.serialization.setSerializationEngine = function(protocolName, serializationEngine) {
  // todo-pre check passed serialization engine
  Bristleback.serialization.serializationEngines[protocolName] = serializationEngine;
};

Bristleback.serialization.JsonEngine = function () {
};

Bristleback.serialization.JsonEngine.prototype.serialize = function (objectToSerialize) {
  return JSON.stringify(objectToSerialize);
};

Bristleback.serialization.JsonEngine.prototype.deserialize = function (serializedObject) {
  return JSON.parse(serializedObject);
};

//------------- DEFAULT SERIALIZATION ENGINES

Bristleback.serialization.serializationEngines["json"] = Bristleback.serialization.JsonEngine;