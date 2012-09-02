Bristleback.serialization.serializationEngines = {
};

Bristleback.serialization.setSerializationEngine = function(protocolName, serializationEngine) {
  // todo-pre check passed serialization engine
  Bristleback.serialization.serializationEngines[protocolName] = serializationEngine;
};

Bristleback.serialization.JsonJQueryEngine = function () {
};

Bristleback.serialization.JsonJQueryEngine.prototype.serialize = function (objectToSerialize) {
  return JSON.stringify(objectToSerialize);
};

Bristleback.serialization.JsonJQueryEngine.prototype.deserialize = function (serializedObject) {
  return jQuery.parseJSON(serializedObject);
};

//------------- DEFAULT SERIALIZATION ENGINES

Bristleback.serialization.serializationEngines["json"] = Bristleback.serialization.JsonJQueryEngine;