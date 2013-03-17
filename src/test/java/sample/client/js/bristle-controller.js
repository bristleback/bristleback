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
Bristleback.controller.controllers = {};

/**
 * Creates a new action message
 * @param {Object} controller data controller
 * @param {Object} message message to sent
 * @class ActionMessage
 * @namespace Bristleback.controller
 * @constructor
 * @private
 */
Bristleback.controller.ActionMessage = function (controller, message) {
  var messageElements = message.name.split(":");
  var actionElements = messageElements[0].split(".");
  var actionClassName = actionElements[0];
  var actionName = actionElements[1] ? actionElements[1] : "";
  if (message.id) {
    this.actionClass = controller.actionClasses[actionClassName];
  } else {
    this.actionClass = controller.clientActionClasses[actionClassName];
  }

  if (this.actionClass == undefined) {
    var errorMsg = "[ERROR] Cannot find a client action class \"" + actionClassName + "\"";
    Bristleback.Console.log(errorMsg);
    throw new Error(errorMsg);
  }

  this.action = this.actionClass.actions[actionName];
  if (this.action == undefined) {
    errorMsg = "[ERROR] Cannot find action " + (actionName ? "\"" + actionName + "\"" : "default action ") + " in action class \"" + actionClassName + "\"";
    Bristleback.Console.log(errorMsg);
    throw new Error(errorMsg);
  }

  this.callback = controller.callbacks[message.id];
  this.content = message.payload;
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
Bristleback.controller.ActionExceptionHandler = function ActionExceptionHandler() {
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
Bristleback.controller.ActionExceptionHandler.prototype.setDefaultExceptionHandler = function (handlerFunction) {
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
Bristleback.controller.ActionExceptionHandler.prototype.setExceptionHandler = function (exceptionType, handlerFunction) {
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
 * @param {String} containerDiv id of the parent container for rendered template.
 */
Bristleback.controller.ActionExceptionHandler.prototype.renderOnException = function (exceptionType, templateName, containerDiv) {
  var templateInformation = Bristleback.templateController.constructTemplateInformation(templateName, containerDiv, "exception");
  this.renderingHandlers[exceptionType] = function (exceptionMessage) {
    Bristleback.templateController.render(templateInformation, exceptionMessage);
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
 * @param templateName
 * @param containerDiv
 * @return {*}
 */
Bristleback.controller.ActionExceptionHandler.prototype.renderOnDefaultException = function (templateName, containerDiv) {
  var templateInformation = Bristleback.templateController.constructTemplateInformation(templateName, containerDiv, "exception");
  this.defaultRenderingHandler = function (exceptionMessage) {
    Bristleback.templateController.render(templateInformation, exceptionMessage);
  };
  return this;
};


Bristleback.controller.ActionExceptionHandler.prototype.handleException = function (exceptionMessage) {
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

Bristleback.controller.ActionExceptionHandler.prototype.handleDefault = function (exceptionMessage) {
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
Bristleback.controller.ActionCallback = function (responseHandler) {

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
  this.exceptionHandler = new Bristleback.controller.ActionExceptionHandler();
};

Bristleback.controller.ActionCallback.prototype.handleResponse = function (content) {
  return this.responseHandler(content);
};

Bristleback.controller.ActionCallback.prototype.canHandleResponse = function () {
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
Bristleback.controller.ActionController = function () {
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
  this.exceptionHandler = new Bristleback.controller.ActionExceptionHandler();

  this.exceptionHandler.setDefaultExceptionHandler(this.defaultHandlerFunction);
};

Bristleback.controller.ActionController.prototype.onMessage = function (message) {
  var actionMessage = new Bristleback.controller.ActionMessage(this, message);
  if (actionMessage.exceptionType) {
    this.onExceptionMessage(actionMessage);
  } else {
    actionMessage.actionClass.onMessage(actionMessage);
  }
};

Bristleback.controller.ActionController.prototype.sendMessage = function (actionClass, action, parameters) {
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
Bristleback.controller.ActionController.prototype.getActionClass = function (actionClassName) {
  var actionClass = this.actionClasses[actionClassName];
  if (actionClass === undefined) {
    actionClass = new Bristleback.controller.ActionClass(actionClassName, this);
    this.actionClasses[actionClassName] = actionClass;
  }
  return actionClass;
};

Bristleback.controller.ActionController.prototype.onExceptionMessage = function (exceptionMessage) {
  return (exceptionMessage.callback && exceptionMessage.callback.exceptionHandler.handleException(exceptionMessage)) ||
    exceptionMessage.action.exceptionHandler.handleException(exceptionMessage) ||
    exceptionMessage.actionClass.exceptionHandler.handleException(exceptionMessage) ||
    this.exceptionHandler.handleException(exceptionMessage);
};

Bristleback.controller.ActionController.prototype.defaultHandlerFunction = function (exceptionMessage) {
  var actionToString = "[" + (exceptionMessage.action.name ? "Action " + exceptionMessage.actionClass.name + "."
    + exceptionMessage.action.name + "()" : "Default action of class " + exceptionMessage.actionClass.name) + "]";
  var exceptionMessageString = actionToString
    + " returned with exception of type \"" + exceptionMessage.exceptionType + "\" and detail message \"" + Bristleback.utils.objectToString(exceptionMessage.content) + "\"";
  Bristleback.Console.log("[ERROR] " + exceptionMessageString);
  throw new Error(exceptionMessageString);
};

/**
 * Registers given object as client action class.
 * After registration, server is able to invoke methods of given client action class.
 * Client action classes are normal Object instances created in any way.
 * @method registerClientActionClass
 * @param  {String} actionClassName name of client action class
 * @param {Object} actionClass client action class object.
 */
Bristleback.controller.ActionController.prototype.registerClientActionClass = function (actionClassName, actionClass) {
  this.clientActionClasses[actionClassName] = new Bristleback.controller.ClientActionClass(actionClassName, actionClass);
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
Bristleback.controller.ActionClass = function (name, actionController) {
  this.actionController = actionController;
  this.name = name;
  this.actions = {};

  /**
   * Exception handler object for specifying reaction for exception responses.
   *
   * @property exceptionHandler
   * @type Bristleback.controller.ActionExceptionHandler
   **/
  this.exceptionHandler = new Bristleback.controller.ActionExceptionHandler();
  this.exceptionHandler.setExceptionHandler("BrokenActionProtocolException", this.defaultProtocolExceptionHandlerFunction);
};

/**
 * Creates a default action definition for this action class.
 * Default actions don't have names (message name consists of action class name only).
 * To invoke default action on server, use {{#crossLink "Bristleback.controller.ActionClass/executeDefault"}}{{/crossLink}}
 * @method defineDefaultAction
 */
Bristleback.controller.ActionClass.prototype.defineDefaultAction = function () {
  return this.defineAction("");
};

/**
 * Creates a non default action definition with name given as parameter.
 * In addition, the action controller creates method and attaches it to this action class.
 * User can then invoke created action like any other methods.
 * @method defineAction
 * @param {String} actionName name of action
 */
Bristleback.controller.ActionClass.prototype.defineAction = function (actionName) {
  if (this[actionName] != undefined) {
    throw new Error("Action " + actionName + " already defined for action class " + this.name);
  }
  this.actions[actionName] = new Bristleback.controller.Action(actionName);
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
 * or {{#crossLink "Bristleback.CONNECTOR"}}{{/crossLink}} object.
 * @method executeDefault
 * @param {String} connector user connection placeholder,
 * {{#crossLink "Bristleback.CONNECTOR"}}{{/crossLink}} constant should be used.
 * @param {Object} payload payload object
 */
Bristleback.controller.ActionClass.prototype.executeDefault = function (connector, payload) {
  var defaultAction = this.actions[""];
  this.doSendMessage(defaultAction, arguments);
};

/**
 * Gets an action definition with name given as parameter.
 * @method getAction
 * @param {String} actionName action name.
 */
Bristleback.controller.ActionClass.prototype.getAction = function (actionName) {
  return this.actions[actionName];
};

/**
 * Gets a default action definition.
 * @method getDefaultAction
 */
Bristleback.controller.ActionClass.prototype.getDefaultAction = function () {
  return this.actions[""];
};

Bristleback.controller.ActionClass.prototype.doSendMessage = function (action, parameters) {
  var correctParameters = [];
  var parametersLength = parameters.length;
  for (var i = 0; i < parametersLength; i++) {
    correctParameters[i] = parameters[i];
  }
  var lastArgument = correctParameters.length == 0 ? undefined : correctParameters[correctParameters.length - 1];
  if (lastArgument != undefined) {
    if (lastArgument instanceof Function) {
      correctParameters.pop();
      var callback = new Bristleback.controller.ActionCallback(lastArgument);
    } else if (lastArgument instanceof Bristleback.controller.ActionCallback) {
      callback = correctParameters.pop();
    }
  }
  var messageId = this.actionController.sendMessage(this.name, action.name, correctParameters);
  if (callback != undefined) {
    this.actionController.callbacks[messageId] = callback;
  }
};

Bristleback.controller.ActionClass.prototype.onMessage = function (actionMessage) {
  if (actionMessage.callback != undefined) {
    this.actionController.callbacks[actionMessage.id] = undefined;
    if (actionMessage.callback.canHandleResponse()) {
      actionMessage.callback.handleResponse(actionMessage.content);
      return;
    }
  }
  this.runHandlers(actionMessage);
};

Bristleback.controller.ActionClass.prototype.runHandlers = function (actionMessage) {
  var action = actionMessage.action;
  if (action.responseHandler == undefined) {
    var actionToString = "[" + (action.name ? "Action " + this.name + "." + action.name + "()" : "Default action of " + this.name) + "]";
    var errorMsg = actionToString + " Cannot find response handler for incoming action";
    Bristleback.Console.log("[ERROR] " + errorMsg);
    throw new Error(errorMsg);
  } else {
    action.responseHandler(actionMessage.content);
  }
  if (action.renderingHandler) {
    action.renderingHandler(actionMessage.content)
  }
};

Bristleback.controller.ActionClass.prototype.defaultProtocolExceptionHandlerFunction = function (exceptionMessage) {
  var protocolViolationType = exceptionMessage.content;
  if (protocolViolationType == 'NO_ACTION_CLASS_FOUND') {
    var exceptionMessageString = "Cannot find action class with name \"" + exceptionMessage.actionClass.name + "\"";
    Bristleback.Console.log("[ERROR] " + exceptionMessageString);
    throw new Error(exceptionMessageString);
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
 */
Bristleback.controller.Action = function (name) {
  this.name = name;

  /**
   * Exception handler object for specifying reaction for exception responses.
   *
   * @property exceptionHandler
   * @type Bristleback.controller.ActionExceptionHandler
   **/
  this.exceptionHandler = new Bristleback.controller.ActionExceptionHandler();
  this.exceptionHandler.setExceptionHandler("BrokenActionProtocolException", this.defaultProtocolExceptionHandlerFunction);
};

/**
 * Sets a response handler function for this action.
 * @method setResponseHandler
 * @chainable
 * @param {Function} handler handler function taking one parameter (actual response object from server).
 */
Bristleback.controller.Action.prototype.setResponseHandler = function (handler) {
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
 * @param {String} containerDiv id of the parent container for rendered template.
 * @param {String} rootObjectName if specified,
 * template controller will prepare literal object containing property with name given by this parameter
 * and value returned by server.
 */
Bristleback.controller.Action.prototype.renderOnResponse = function (templateName, containerDiv, rootObjectName) {
  var templateInformation = Bristleback.templateController.constructTemplateInformation(templateName, containerDiv, rootObjectName);

  this.renderingHandler = function (actionMessage) {
    Bristleback.templateController.render(templateInformation, actionMessage);
  };
  return this;
};

Bristleback.controller.Action.prototype.defaultProtocolExceptionHandlerFunction = function (exceptionMessage) {
  var protocolViolationType = exceptionMessage.content;
  if (protocolViolationType == 'NO_DEFAULT_ACTION_FOUND') {
    var exceptionMessageString = "Cannot find default action in action class with name \"" + exceptionMessage.actionClass.name + "\"";
  } else if (protocolViolationType == 'NO_ACTION_FOUND') {
    exceptionMessageString = "Cannot find action \"" + exceptionMessage.action.name + "\" in action class with name \"" + exceptionMessage.actionClass.name + "\"";
  } else {
    return false;
  }
  Bristleback.Console.log("[ERROR] " + exceptionMessageString);
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
Bristleback.controller.ClientActionClass = function (name, actionClass) {
  this.name = name;
  this.actions = actionClass;
};

Bristleback.controller.ClientActionClass.prototype.onMessage = function (actionMessage) {
  var parameters = [];
  var hasMoreParams = true;
  var currentIndex = 0;
  if (actionMessage.content != undefined && actionMessage.content != null) {
    while (hasMoreParams) {
      var paramName = "p" + currentIndex;
      var parameter = actionMessage.content[paramName];
      if (parameter != undefined) {
        parameters[currentIndex] = parameter;
        currentIndex++;
      } else {
        hasMoreParams = false;
      }
    }
  }
  if (parameters.length == 0) {
    parameters[0] = actionMessage.content;
  }

  actionMessage.action.apply(actionMessage.actionClass, parameters);
};

//------------- DEFAULT CONTROLLERS

Bristleback.controller.controllers["system.controller.action"] = Bristleback.controller.ActionController;
