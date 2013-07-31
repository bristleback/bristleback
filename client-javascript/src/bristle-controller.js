/**
 Action controller module

 @module Bristleback
 @submodule controller
 @main Bristleback
 **/

/**
 * Controllers container map.
 * @static
 * @class controllers
 * @namespace Bristleback.controller
 * @type Object
 */
BB.controller.controllers = {};

/**
 * Creates a new action message
 * @param {Object} controller data controller
 * @param {Object} message message to sent
 * @class ActionMessage
 * @namespace Bristleback.controller
 * @constructor
 * @private
 */
BB.controller.ActionMessage = function (controller, message) {
  var messageElements = message.name.split(":");
  var actionElements = messageElements[0].split(".");
  var actionClassName = actionElements[0];
  var actionName = actionElements[1] ? actionElements[1] : "";
  if (message.id) {
    this.actionClass = controller.actionClasses[actionClassName];
    this.content = message.payload[0];
  } else {
    this.actionClass = controller.clientActionClasses[actionClassName];
    this.content = message.payload;
  }

  if (this.actionClass == undefined) {
    throw new Error("[ERROR] Cannot find a client action class \"" + actionClassName + "\"");
  }

  this.action = this.actionClass.actions[actionName];
  if (this.action == undefined) {
    throw new Error("[ERROR] Cannot find action " + (actionName ? "\"" + actionName + "\"" : "default action ") + " in action class \"" + actionClassName + "\"");
  }

  this.callback = controller.callbacks[message.id];
  this.exceptionType = messageElements.length > 1 ? this.content.type : undefined;
};

/**
 * Action exception handler class allows to fully control exception handling process.
 * Instance of this class is created for every action, action class, action callback object and
 * also there is one exception handler in action controller object.
 * @class ActionExceptionHandler
 * @namespace Bristleback.controller
 * @constructor
 */
BB.controller.ActionExceptionHandler = function ActionExceptionHandler() {
  this.defaultExceptionHandler = undefined;
  this.defaultRenderingHandler = undefined;
  this.exceptionHandlers = {};
  this.renderingHandlers = {};
};

/**
 * Sets a default exception handler function that will be invoked when exception handler for given exception type
 * cannot be found or it has been found and returned "false".
 * @method setDefaultExceptionHandler
 * @chainable
 * @param {Function} handlerFunction exception handler function reference, containing one parameter, which is actual exception message.
 */
BB.controller.ActionExceptionHandler.prototype.setDefaultExceptionHandler = function (handlerFunction) {
  this.defaultExceptionHandler = handlerFunction;
  return this;
};

/**
 * Sets exception handler function applicable for exception type given as first parameter.
 * @method setExceptionHandler
 * @chainable
 * @param {String} exceptionType exception type
 * @param {Function} handlerFunction exception handler function reference, containing one parameter, which is actual exception message.
 */
BB.controller.ActionExceptionHandler.prototype.setExceptionHandler = function (exceptionType, handlerFunction) {
  this.exceptionHandlers[exceptionType] = handlerFunction;
  return this;
};

/**
 * Adds rendering handler to be used when given exception type occurs.
 * Before using rendering functionality, templating framework implementation should be provided by calling
 * Bristleback.templateController.registerTemplateFramework() method.
 * Rendering handlers ALWAYS break exception processing cycle.
 * @method renderOnException
 * @chainable
 * @param {String} exceptionType exception type to handle.
 * @param {String} templateName template name that should be used.
 * @param {String} containerId id of the parent container for rendered template.
 * @param {String} renderingMode name of rendering mode, one of the specified in
 * <strong>Bristleback.templateController.renderingModes</strong> map. By default, "replace" mode is used.
 */
BB.controller.ActionExceptionHandler.prototype.renderOnException = function (exceptionType, templateName, containerId, renderingMode) {
  var templateInformation = BB.templateController.constructTemplateInformation(templateName, containerId, "exception", renderingMode);
  this.renderingHandlers[exceptionType] = function (exceptionMessage) {
    BB.templateController.render(templateInformation, exceptionMessage);
  };
  return this;
};

/**
 * Adds default rendering handler to be used when exception occurs.
 * Before using rendering functionality, templating framework implementation should be provided by calling
 * Bristleback.templateController.registerTemplateFramework() method.
 * Rendering handlers ALWAYS break exception processing cycle.
 * @method renderOnDefaultException
 * @chainable
 * @param templateName template name that should be used.
 * @param containerId id of the parent container for rendered template.
 * @param {String} renderingMode name of rendering mode, one of the specified in
 * <strong>Bristleback.templateController.renderingModes</strong> map. By default, "replace" mode is used.
 */
BB.controller.ActionExceptionHandler.prototype.renderOnDefaultException = function (templateName, containerId, renderingMode) {
  var templateInformation = BB.templateController.constructTemplateInformation(templateName, containerId, "exception", renderingMode);
  this.defaultRenderingHandler = function (exceptionMessage) {
    BB.templateController.render(templateInformation, exceptionMessage);
  };
  return this;
};


BB.controller.ActionExceptionHandler.prototype.handleException = function (exceptionMessage) {
  var chosenHandler = this.exceptionHandlers[exceptionMessage.exceptionType];
  var chosenRenderingHandler = this.renderingHandlers[exceptionMessage.exceptionType];
  var breakChain = false;
  if (chosenHandler) {
    breakChain = chosenHandler(exceptionMessage);
  }
  if (chosenRenderingHandler) {
    breakChain = true;
    chosenRenderingHandler(exceptionMessage);
  }

  return breakChain || this.handleDefault(exceptionMessage);
};

BB.controller.ActionExceptionHandler.prototype.handleDefault = function (exceptionMessage) {
  var breakChain = false;
  if (this.defaultExceptionHandler) {
    breakChain = this.defaultExceptionHandler(exceptionMessage);
  }
  if (this.defaultRenderingHandler) {
    breakChain = true;
    this.defaultRenderingHandler(exceptionMessage);
  }
  return breakChain;
};


//------------- ACTION CALLBACK

/**
 * This class is the highest level response handler used in server actions.
 * Action callbacks can be placed as last parameter in server action invocations.
 * Action callback contains fields for both normal and exception responses.
 * @class ActionCallback
 * @namespace Bristleback.controller
 * @constructor
 * @param {Function} responseHandler handler function taking one parameter (actual response object from server).
 */
BB.controller.ActionCallback = function (responseHandler) {

  /**
   * Handler function taking one parameter (actual response object from server).
   *  This handler is invoked in case when non exceptional response is returned by the server.
   * @property responseHandler
   * @type Function
   */
  this.responseHandler = responseHandler;

  /**
   * Exception handler object for specifying reaction for exception responses.
   *
   * @property exceptionHandler
   * @type Bristleback.controller.ActionExceptionHandler
   **/
  this.exceptionHandler = new BB.controller.ActionExceptionHandler();
};

BB.controller.ActionCallback.prototype.handleResponse = function (content) {
  return this.responseHandler(content);
};

BB.controller.ActionCallback.prototype.canHandleResponse = function () {
  return this.responseHandler != undefined;
};

//------------- ACTION CONTROLLER

/**
 * Action controller is a default, built in data controller used in Bristleback Server.
 * Action controller uses server and client action classes to communicate with server.
 * Name of this controller: 'system.controller.action'. To use action controller,
 * server must have it enabled in configuration.
 * @class ActionController
 * @namespace Bristleback.controller
 * @constructor
 */
BB.controller.ActionController = function () {
  this.client = undefined;
  this.lastId = 1;

  this.actionClasses = {};
  this.clientActionClasses = {};
  this.callbacks = {};

  /**
   * Exception handler object for specifying reaction for exception responses.
   *
   * @property exceptionHandler
   * @type Bristleback.controller.ActionExceptionHandler
   **/
  this.exceptionHandler = new BB.controller.ActionExceptionHandler();

  this.exceptionHandler.setDefaultExceptionHandler(this.defaultHandlerFunction);

  this.authentication = new BB.auth.SystemAuthentication(this);
};

BB.controller.ActionController.prototype.onMessage = function (message) {
  var actionMessage = new BB.controller.ActionMessage(this, message);
  if (actionMessage.exceptionType) {
    this.onExceptionMessage(actionMessage);
  } else {
    actionMessage.actionClass.onMessage(actionMessage);
  }
};

BB.controller.ActionController.prototype.sendMessage = function (actionClass, action, parameters) {
  var currentId = this.lastId++;
  var messageName = action ? actionClass + "." + action : actionClass;
  var message = {
    name: messageName,
    payload: parameters,
    id: currentId
  };
  this.client.sendMessage(message);
  return currentId;
};

/**
 * Gets a server action class with name given as parameter.
 * If action class doesn't exist, this method transparently creates one
 * and immediately returns it.
 * @method getActionClass
 * @param {String} actionClassName name of requested server action class.
 */
BB.controller.ActionController.prototype.getActionClass = function (actionClassName) {
  var actionClass = this.actionClasses[actionClassName];
  if (actionClass === undefined) {
    actionClass = new BB.controller.ActionClass(actionClassName, this);
    this.actionClasses[actionClassName] = actionClass;
  }
  return actionClass;
};

BB.controller.ActionController.prototype.onExceptionMessage = function (exceptionMessage) {
  return (exceptionMessage.callback && exceptionMessage.callback.exceptionHandler.handleException(exceptionMessage)) ||
    exceptionMessage.action.exceptionHandler.handleException(exceptionMessage) ||
    exceptionMessage.actionClass.exceptionHandler.handleException(exceptionMessage) ||
    this.exceptionHandler.handleException(exceptionMessage);
};

BB.controller.ActionController.prototype.defaultHandlerFunction = function (exceptionMessage) {
  var actionToString = "[" + (exceptionMessage.action.name ? "Action " + exceptionMessage.actionClass.name + "."
    + exceptionMessage.action.name + "()" : "Default action of class " + exceptionMessage.actionClass.name) + "]";
  throw new Error(actionToString
    + " returned with exception of type \"" + exceptionMessage.exceptionType + "\" and detail message \""
    + BB.utils.objectToString(exceptionMessage.content) + "\"");
};

/**
 * Registers given object as client action class.
 * After registration, server is able to invoke methods of given client action class.
 * Client action classes are normal Object instances created in any way.
 * @method registerClientActionClass
 * @param  {String} actionClassName name of client action class
 * @param {Object} actionClass client action class object.
 */
BB.controller.ActionController.prototype.registerClientActionClass = function (actionClassName, actionClass) {
  this.clientActionClasses[actionClassName] = new BB.controller.ClientActionClass(actionClassName, actionClass);
};


//------------- ACTION CLASS

/**
 * ActionClass is a client representation of server action class defined on Java side.
 * Using this class, user can invoke server actions and specify how to handle normal/exception responses.
 * Action class instances should not be created using directly this constructors,
 * but using {{#crossLink "Bristleback.controller.ActionController/getActionClass"}}{{/crossLink}} method
 *
 * @class ActionClass
 * @namespace Bristleback.controller
 * @constructor
 * @param {String} name name of this action class.
 * @param {Object} actionController
 */
BB.controller.ActionClass = function (name, actionController) {
  this.actionController = actionController;
  this.name = name;
  this.actions = {};

  /**
   * Exception handler object for specifying reaction for exception responses.
   *
   * @property exceptionHandler
   * @type Bristleback.controller.ActionExceptionHandler
   **/
  this.exceptionHandler = new BB.controller.ActionExceptionHandler();
  this.exceptionHandler.setExceptionHandler("BrokenActionProtocolException", this.defaultProtocolExceptionHandlerFunction);
};

/**
 * Creates a default action definition for this action class.
 * Default actions don't have names (message name consists of action class name only).
 * To invoke default action on server, use {{#crossLink "Bristleback.controller.ActionClass/executeDefault"}}{{/crossLink}}
 * @method defineDefaultAction
 */
BB.controller.ActionClass.prototype.defineDefaultAction = function () {
  return this.defineAction("");
};

/**
 * Creates a non default action definition with name given as parameter.
 * In addition, the action controller creates method and attaches it to this action class.
 * User can then invoke created action like any other methods.
 * @method defineAction
 * @param {String} actionName name of action
 */
BB.controller.ActionClass.prototype.defineAction = function (actionName) {
  if (this[actionName] != undefined) {
    throw new Error("Action " + actionName + " already defined for action class " + this.name);
  }
  this.actions[actionName] = new BB.controller.Action(actionName, this);
  this[actionName] = function () {
    this.doSendMessage(this.actions[actionName], arguments);
  };
  return this.actions[actionName];
};

/**
 * Invokes default action of this action class on the server side.
 * Action execution is always asynchronous.
 * Response handler can be specified by setting handler method in
 * {{#crossLink "Bristleback.controller.Action"}}{{/crossLink}} object
 * or by adding additional parameter at the end of parameters list.
 * Such additional parameter can be function (which will be used when normal, non exceptional response arrives)
 * or {{#crossLink "Bristleback.USER_CONTEXT"}}{{/crossLink}} object.
 * @method executeDefault
 * @param {String} connector user connection placeholder,
 * {{#crossLink "Bristleback.USER_CONTEXT"}}{{/crossLink}} constant should be used.
 * @param {Object} payload payload object
 */
BB.controller.ActionClass.prototype.executeDefault = function (connector, payload) {
  var defaultAction = this.actions[""];
  this.doSendMessage(defaultAction, arguments);
};

/**
 * Gets an action definition with name given as parameter.
 * @method getAction
 * @param {String} actionName action name.
 */
BB.controller.ActionClass.prototype.getAction = function (actionName) {
  return this.actions[actionName];
};

/**
 * Gets a default action definition.
 * @method getDefaultAction
 */
BB.controller.ActionClass.prototype.getDefaultAction = function () {
  return this.actions[""];
};

BB.controller.ActionClass.prototype.doSendMessage = function (action, parameters) {
  var correctParameters = [];
  var parametersLength = parameters.length;
  for (var i = 0; i < parametersLength; i++) {
    correctParameters[i] = parameters[i];
  }
  var lastArgument = correctParameters.length == 0 ? undefined : correctParameters[correctParameters.length - 1];
  if (lastArgument != undefined) {
    if (lastArgument instanceof Function) {
      correctParameters.pop();
      var callback = new BB.controller.ActionCallback(lastArgument);
    } else if (lastArgument instanceof BB.controller.ActionCallback) {
      callback = correctParameters.pop();
    }
  }
  var messageId = this.actionController.sendMessage(this.name, action.name, correctParameters);
  if (callback != undefined) {
    this.actionController.callbacks[messageId] = callback;
  }
};

BB.controller.ActionClass.prototype.onMessage = function (actionMessage) {
  if (actionMessage.callback != undefined) {
    this.actionController.callbacks[actionMessage.id] = undefined;
    if (actionMessage.callback.canHandleResponse()) {
      actionMessage.callback.handleResponse(actionMessage.content);
      return;
    }
  }
  this.runHandlers(actionMessage);
};

BB.controller.ActionClass.prototype.runHandlers = function (actionMessage) {
  var action = actionMessage.action;
  if (action.responseHandler == undefined) {
    var actionToString = "[" + (action.name ? "Action " + this.name + "." + action.name + "()" : "Default action of " + this.name) + "]";
    throw new Error(actionToString + " Cannot find response handler for incoming action");
  } else {
    action.responseHandler(actionMessage.content);
  }
  if (action.renderingHandler) {
    action.renderingHandler(actionMessage.content)
  }
};

BB.controller.ActionClass.prototype.defaultProtocolExceptionHandlerFunction = function (exceptionMessage) {
  var protocolViolationType = exceptionMessage.content;
  if (protocolViolationType == 'NO_ACTION_CLASS_FOUND') {
    throw new Error("Cannot find action class with name \"" + exceptionMessage.actionClass.name + "\"");
  } else {
    return false;
  }
};

//------------- ACTION

/**
 * This is a single server action definition, created within a server action class.
 * This class provides fluent API for building action behaviour.
 * Constructor of this class should not be used directly by application user.
 * User should rather create action using {{#crossLink "Bristleback.controller.ActionClass/defineDefaultAction"}}{{/crossLink}}
 * and {{#crossLink "Bristleback.controller.ActionClass/defineAction"}}{{/crossLink}} .
 * @class Action
 * @namespace Bristleback.controller
 * @param name name of this action
 * @param actionClass action class containing this action
 */
BB.controller.Action = function (name, actionClass) {
  this.name = name;
  this.actionClass = actionClass;

  /**
   * Exception handler object for specifying reaction for exception responses.
   *
   * @property exceptionHandler
   * @type Bristleback.controller.ActionExceptionHandler
   **/
  this.exceptionHandler = new BB.controller.ActionExceptionHandler();
  this.exceptionHandler.setExceptionHandler("BrokenActionProtocolException", this.defaultProtocolExceptionHandlerFunction);
};

/**
 * Sets a response handler function for this action.
 * @method setResponseHandler
 * @chainable
 * @param {Function} handler handler function taking one parameter (actual response object from server).
 */
BB.controller.Action.prototype.setResponseHandler = function (handler) {
  this.responseHandler = handler;
  return this;
};

/**
 * Sets a rendering response handler function for this action.
 * If there is another response handler defined for this action (by invoking setResponseHandler() method),
 * rendering handler function runs after that response handler, so normal handler can modify or prepare action response.
 * @method renderOnResponse
 * @chainable
 * @param {String} templateName template name that should be used.
 * @param {String} containerId id of the parent container for rendered template.
 * @param {String} rootObjectName if specified,
 * template controller will prepare literal object containing property with name given by this parameter
 * and value returned by server.
 * @param {String} renderingMode name of rendering mode, one of the specified in
 * <strong>Bristleback.templateController.renderingModes</strong> map. By default, "replace" mode is used.
 */
BB.controller.Action.prototype.renderOnResponse = function (templateName, containerId, rootObjectName, renderingMode) {
  var templateInformation = BB.templateController.constructTemplateInformation(templateName, containerId, rootObjectName, renderingMode);

  this.renderingHandler = function (actionMessage) {
    BB.templateController.render(templateInformation, actionMessage);
  };
  return this;
};

BB.controller.Action.prototype.defaultProtocolExceptionHandlerFunction = function (exceptionMessage) {
  var protocolViolationType = exceptionMessage.content;
  if (protocolViolationType == 'NO_DEFAULT_ACTION_FOUND') {
    var exceptionMessageString = "Cannot find default action in action class with name \"" + exceptionMessage.actionClass.name + "\"";
  } else if (protocolViolationType == 'NO_ACTION_FOUND') {
    exceptionMessageString = "Cannot find action \"" + exceptionMessage.action.name + "\" in action class with name \"" + exceptionMessage.actionClass.name + "\"";
  } else {
    return false;
  }
  throw new Error(exceptionMessageString);
};

//------------- CLIENT ACTION CLASS

/**
 * This is a client action class definition.
 * Instances of this class should be created directly by application user.
 * Instead, they can use {{#crossLink "Bristleback.controller.ActionController/registerClientActionClass"}}{{/crossLink}}
 * @class ClientActionClass
 * @namespace Bristleback.controller
 * @constructor
 * @param {String} name name of this client action class
 * @param {Object} actionClass real client action class instance.
 */
BB.controller.ClientActionClass = function (name, actionClass) {
  this.name = name;
  this.actions = actionClass;
};

BB.controller.ClientActionClass.prototype.onMessage = function (actionMessage) {
  actionMessage.action.apply(actionMessage.actionClass, actionMessage.content);
};

//------------- DEFAULT CONTROLLERS

BB.controller.controllers["system.controller.action"] = BB.controller.ActionController;
