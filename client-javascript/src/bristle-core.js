/**
 Core module

 @module Bristleback
 @submodule core
 @main Bristleback
 **/

/**
 * This class is a WebSocket connection abstraction. It uses WebSocket object to connect and communicate with the server.
 * Single Client object represents a single client-server connection. There can be multiple instances of this class
 * (there can be multiple connections from single browser tab).
 * @class Client
 * @namespace Bristleback
 * @param config configuration object.
 */
BB.Client = function (config) {
  this.connection = undefined;

  config = config || {};

  config.serverUrl = config.serverUrl || BB.LOCAL_HOSTNAME;
  config.serializationEngine = config.serializationEngine || "json";
  config.dataController = config.dataController || "system.controller.action";
  config.timeout = config.timeout || 360000;
  config.developmentConsole = config.developmentConsole || false;

  this.dataController = new BB.controller.controllers[config.dataController]();
  this.dataController.client = this;

  this.serializationEngine = new BB.serialization.serializationEngines[config.serializationEngine]();

  config.onOpen = config.onOpen || function () {
    console.log("connected");
    alert("connected");
  };

  config.onClose = config.onClose || function () {
    console.log("disconnected");
    alert("disconnected");
  };

  var thisClient = this;
  config.onMessage = config.onMessage || function (event) {
    var deserializedEvent = thisClient.serializationEngine.deserialize(event.data);
    thisClient.dataController.onMessage(deserializedEvent);
  };

  this.configuration = config;
};

/**
 * Gets current client connection state. The returned value is equals to the one of following fields:
 *
 *  * WebSocket.CONNECTING
 *  * WebSocket.OPEN
 *  * WebSocket.CLOSING
 *  * WebSocket.CLOSED
 *  @method getConnectionState
 */
BB.Client.prototype.getConnectionState = function () {
  if (!this.connection) {
    return WebSocket.CLOSED;
  }
  return this.connection.readyState;
};

/**
 * Checks whether this client is connected (has connection state equals to WebSocket.OPEN).
 * @method isConnected
 */
BB.Client.prototype.isConnected = function () {
  return this.getConnectionState() == WebSocket.OPEN;
};

/**
 * Checks whether this client is disconnected (has connection state equals to WebSocket.CLOSED).
 * @method isDisconnected
 */
BB.Client.prototype.isDisconnected = function () {
  return this.getConnectionState() == WebSocket.CLOSED;
};

/**
 * Connects to remote server using configuration passed as constructor parameter.
 * If connection is not closed in the moment of connection, exception is thrown.
 * @method connect
 */
BB.Client.prototype.connect = function () {
  if (!this.isDisconnected()) {
    throw new Error("Connection is not closed, cannot establish another connection.");
  }
  this.connection = new WebSocket(this.configuration.serverUrl, this.configuration.dataController);
  this.connection.onopen = this.configuration.onOpen;
  this.connection.onmessage = this.configuration.onMessage;
  this.connection.onclose = this.configuration.onClose;
};

/**
 * Sends a message to remote server. Message is serialized using client serialization engine.
 * This method should generally be not used by application creator. Instead, data controller should be user.
 * @method sendMessage
 * @param message message object to send.
 */
BB.Client.prototype.sendMessage = function (message) {
  if (!this.isConnected()) {
    throw new Error("Cannot send a message, connection is not open.");
  }
  var serializedMessage = this.serializationEngine.serialize(message);
  this.connection.send(serializedMessage);
};

/**
 * Disconnects client from remote server.
 * If connection is not open, exception is thrown.
 * @method disconnect
 */
BB.Client.prototype.disconnect = function () {
  if (!this.isConnected()) {
    throw new Error("Connection is not open, it may be already closed or in closing state. Actual state: " + this.getConnectionState());
  }
  this.connection.close();
};

/**
 * Creates a new instance of client, using passed configuration object.
 * @method newClient
 * @static
 * @param config
 */
BB.newClient = function (config) {
  return new BB.Client(config);
};