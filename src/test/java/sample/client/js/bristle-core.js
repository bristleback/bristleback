Bristleback.Client = function(config) {
  this.connection = undefined;

  config = config || {};

  config.serverUrl = config.serverUrl || Bristleback.LOCAL_HOSTNAME;
  config.serializationEngine = config.protocol || "json";
  config.dataController = config.protocol || "system.controller.action";
  config.timeout = config.timeout || 360000;
  config.developmentConsole = config.developmentConsole || false;

  this.dataController = new Bristleback.controller.controllers[config.dataController]();
  this.dataController.client = this;

  this.serializationEngine = new Bristleback.serialization.serializationEngines[config.serializationEngine]();

  config.OnOpen = config.OnOpen || function() {
    alert("connected");
  };

  config.OnClose = config.OnClose || function() {
    alert("disconnected");
  };

  var thisClient = this;
  config.OnMessage = config.OnMessage || function(event) {
    var deserializedEvent = thisClient.serializationEngine.deserialize(event.data);
    thisClient.dataController.onMessage(deserializedEvent);
  };

  this.configuration = config;
};

Bristleback.Client.prototype.getConnectionState = function() {
  if (!this.connection) {
    return WebSocket.CLOSED;
  }
  return this.connection.readyState;
};

Bristleback.Client.prototype.isConnected = function() {
  return this.getConnectionState() == WebSocket.OPEN;
};

Bristleback.Client.prototype.isDisconnected = function() {
  return this.getConnectionState() == WebSocket.CLOSED;
};

Bristleback.Client.prototype.connect = function() {
  if (!this.isDisconnected()) {
    var msg = "Connection is not closed, cannot establish another connection.";
    Bristleback.Console.log(msg);
    throw new Error(msg);
  }
  this.connection = new WebSocket(this.configuration.serverUrl, this.configuration.dataController);
  this.connection.onopen = this.configuration.OnOpen;
  this.connection.onmessage = this.configuration.OnMessage;
  this.connection.onclose = this.configuration.OnClose;
};

Bristleback.Client.prototype.sendMessage = function(message) {
  if (!this.isConnected()) {
    var msg = "Cannot send a message, connection is not open.";
    Bristleback.Console.log(msg);
    throw new Error(msg);
  }
  var serializedMessage = this.serializationEngine.serialize(message);
  this.connection.send(serializedMessage);
};

Bristleback.Client.prototype.disconnect = function() {
  if (!this.isConnected()) {
    var msg = "Connection is not open, it may be already closed or in closing state. Actual state: " + this.getConnectionState();
    Bristleback.Console.log(msg);
    throw new Error(msg);
  }
  this.connection.close();
};

Bristleback.newClient = function(config) {
  return new Bristleback.Client(config);
};