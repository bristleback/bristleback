/**
 Serialization module

 @module Bristleback
 @submodule serialization
 @main Bristleback
 **/

BB.serialization.serializationEngines = {
};

BB.serialization.setSerializationEngine = function(protocolName, serializationEngine) {
  // todo-pre check passed serialization engine
  BB.serialization.serializationEngines[protocolName] = serializationEngine;
};

BB.serialization.JsonEngine = function () {
};

BB.serialization.JsonEngine.prototype.serialize = function (objectToSerialize) {
  return JSON.stringify(objectToSerialize);
};

BB.serialization.JsonEngine.prototype.deserialize = function (serializedObject) {
  return JSON.parse(serializedObject);
};

//------------- DEFAULT SERIALIZATION ENGINES

BB.serialization.serializationEngines["json"] = BB.serialization.JsonEngine;