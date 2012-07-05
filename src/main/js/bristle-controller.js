Bristleback.controller.controllers = {};

Bristleback.controller.Messages = {
  ACTION_CLASS_NOT_FOUND : "[ERROR] Message is not complete, action class name parameter is required"
};

Bristleback.controller.ActionMessage = function(controller, message) {
  var messageElements = message.name.split(":");
  var actionElements = messageElements[0].split(".");
  var actionClassName = actionElements[0];
  var actionName = actionElements[1] ? actionElements[1] : "";
  this.actionClass = controller.actionClasses[actionClassName];
  if (this.actionClass == undefined) {
    Bristleback.Console.log(Bristleback.controller.Messages.ACTION_CLASS_NOT_FOUND);
    throw new Error(Bristleback.controller.Messages.ACTION_CLASS_NOT_FOUND);
  }

  this.action = this.actionClass.actions[actionName];
  if (this.action == undefined) {
    var errorMsg = "[ERROR] Cannot find action " + (actionName ? actionName : "default action ") + " in action class " + this.name;
    Bristleback.Console.log(errorMsg);
    throw new Error(errorMsg);
  }

  this.callback = controller.callbacks[message.id];
  this.content = message.payload;
  this.exceptionType = messageElements.length > 0 ? this.content.type : undefined;
};

//------------- EXCEPTION HANDLER PROTOTYPE

Bristleback.controller.ActionExceptionHandlerFunctions = function() {

  this.setDefaultExceptionHandler = function(handlerFunction) {
    this.defaultExceptionHandler = handlerFunction;
  };

  this.setExceptionHandler = function(exceptionType, handlerFunction) {
    this.exceptionHandlers[exceptionType] = handlerFunction;
  };

  this.handleException = function(exceptionMessage) {
    var chosenHandler = this.exceptionHandlers[exceptionMessage.exceptionType];
    if (!chosenHandler) {
      return this.handleDefault(exceptionMessage);
    }
    var breakChain = chosenHandler(exceptionMessage);
    return breakChain || this.handleDefault(exceptionMessage);
  };

  this.handleDefault = function(exceptionMessage) {
    if (!this.defaultExceptionHandler) {
      return false;
    }
    return this.defaultExceptionHandler(exceptionMessage);
  }
};

Bristleback.controller.ActionExceptionHandler = function ActionExceptionHandler() {
  this.defaultExceptionHandler = undefined;
  this.exceptionHandlers = {};
};
Bristleback.controller.ActionExceptionHandler.prototype = new Bristleback.controller.ActionExceptionHandlerFunctions();

//------------- ACTION CALLBACK

Bristleback.controller.ActionCallback = function(responseHandler) {
  this.responseHandler = responseHandler;
  this.exceptionHandler = new Bristleback.controller.ActionExceptionHandler();
};

Bristleback.controller.ActionCallback.prototype.handleResponse = function(content) {
  return this.responseHandler(content);
};

Bristleback.controller.ActionCallback.prototype.canHandleResponse = function(content) {
  return this.responseHandler != undefined;
};

//------------- TEMPLATE ENGINE

Bristleback.controller.TemplateController = {
  parsedTemplates : {},

  constructTemplateInformation: function(templateName, containerDiv, rootObjectName) {
    containerDiv = containerDiv ? containerDiv : "#" + templateName + "-div";

    var templateInformation = {};
    templateInformation.rootObjectName = rootObjectName;
    templateInformation.containerDiv = containerDiv;
    templateInformation.templateName = templateName;
    return templateInformation;
  },

  containsTemplate: function(templateName) {
    return this.parsedTemplates[templateName] != undefined;
  },

  getTemplate: function(templateName) {
    if (!this.containsTemplate(templateName)) {
      if (!document.getElementById(templateName)) {
        var logMsg = "Cannot find template with id: " + templateName;
        Bristleback.Console.log(logMsg);
        throw new Error(logMsg);
      }
      this.parsedTemplates[templateName] = TrimPath.parseDOMTemplate(templateName);
    }
    return this.parsedTemplates[templateName];
  },

  render: function(templateInformation, object) {
    if (templateInformation.rootObjectName) {
      var data = {};
      data[templateInformation.rootObjectName] = object;
    } else {
      data = object;
    }

    var parsedTemplate = this.getTemplate(templateInformation.templateName);
    var result = parsedTemplate.process(data);
    var idWithoutHash = templateInformation.containerDiv.substring(1);
    var container = document.getElementById(idWithoutHash);
    container.innerHTML = result;
  }
};


//------------- ACTION CONTROLLER

Bristleback.controller.ActionController = function () {
  this.client = undefined;
  this.lastId = 1;

  this.actionClasses = {};
  this.callbacks = {};
  this.exceptionHandler = new Bristleback.controller.ActionExceptionHandler();

  this.exceptionHandler.setDefaultExceptionHandler(this.defaultHandlerFunction);
};

Bristleback.controller.ActionController.prototype.onMessage = function(message) {
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

Bristleback.controller.ActionController.prototype.getActionClass = function (actionClassName) {
  var actionClass = this.actionClasses[actionClassName];
  if (actionClass === undefined) {
    actionClass = new Bristleback.controller.ActionClass(actionClassName, this);
    this.actionClasses[actionClassName] = actionClass;
  }
  return actionClass;
};

Bristleback.controller.ActionController.prototype.onExceptionMessage = function(exceptionMessage) {
  return (exceptionMessage.callback && exceptionMessage.callback.exceptionHandler.handleException(exceptionMessage)) ||
    exceptionMessage.action.exceptionHandler.handleException(exceptionMessage) ||
    exceptionMessage.actionClass.exceptionHandler.handleException(exceptionMessage) ||
    this.exceptionHandler.handleException(exceptionMessage);
};

Bristleback.controller.ActionController.prototype.defaultHandlerFunction = function(exceptionMessage) {
  var actionToString = "[" + (exceptionMessage.action.name ? "Action " + exceptionMessage.actionClass.name + "."
    + exceptionMessage.action.name + "()" : "Default action of class " + exceptionMessage.actionClass.name) + "]";
  var exceptionMessageString = actionToString
    + " returned with exception of type \"" + exceptionMessage.exceptionType + "\" and detail message \"" + Bristleback.utils.objectToString(exceptionMessage.content) + "\"";
  Bristleback.Console.log("[ERROR] " + exceptionMessageString);
  throw new Error(exceptionMessageString);
};

//------------- ACTION CLASS

Bristleback.controller.ActionClass = function (name, actionController) {
  this.actionController = actionController;
  this.name = name;
  this.actions = {};
  this.incomingActionHandlers = {};
  this.exceptionHandler = new Bristleback.controller.ActionExceptionHandler();
  this.exceptionHandler.setExceptionHandler("BrokenActionProtocolException", this.defaultProtocolExceptionHandlerFunction);
};

Bristleback.controller.ActionClass.prototype.defineDefaultAction = function() {
  return this.defineAction("");
};

Bristleback.controller.ActionClass.prototype.defineAction = function(actionName) {
  if (this[actionName] != undefined) {
    throw new Error("Action " + actionName + " already defined for action class " + this.name);
  }
  this.actions[actionName] = new Bristleback.controller.Action(actionName);
  this[actionName] = function() {
    this.doSendMessage(this.actions[actionName], arguments);
  };
  return this.actions[actionName];
};

Bristleback.controller.ActionClass.prototype.executeDefault = function() {
  var defaultAction = this.actions[""];
  this.doSendMessage(defaultAction, arguments);
};

Bristleback.controller.ActionClass.prototype.getAction = function(actionName) {
  return this.actions[actionName];
};

Bristleback.controller.ActionClass.prototype.doSendMessage = function(action, parameters) {
  var correctParameters = [];
  for (var i in parameters) {
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

Bristleback.controller.ActionClass.prototype.onMessage = function(actionMessage) {
  if (actionMessage.callback != undefined) {
    this.actionController.callbacks[actionMessage.id] = undefined;
    if (actionMessage.callback.canHandleResponse()) {
      actionMessage.callback.handleResponse(actionMessage.content);
      return;
    }
  }
  var handler = this.findHandler(actionMessage.action);
  if (handler) {
    handler(actionMessage.content);
  }
};

Bristleback.controller.ActionClass.prototype.findHandler = function(action) {
  if (action.incomingMessageHandler == undefined) {
    var actionToString = "[" + (action.name ? "Action " + this.name + "." + action.name + "()" : "Default action of class " + this.name) + "]";
    var errorMsg = +actionToString + " Cannot find handler for incoming action";
    Bristleback.Console.log("[ERROR] " + errorMsg);
    throw new Error(errorMsg);
  }
  return action.incomingMessageHandler;
};

Bristleback.controller.ActionClass.prototype.defaultProtocolExceptionHandlerFunction = function(exceptionMessage) {
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

Bristleback.controller.Action = function (name) {
  this.name = name;
  this.exceptionHandler = new Bristleback.controller.ActionExceptionHandler();
  this.exceptionHandler.setExceptionHandler("BrokenActionProtocolException", this.defaultProtocolExceptionHandlerFunction);
};

Bristleback.controller.Action.prototype.setIncomingMessageHandler = function(handler) {
  this.incomingMessageHandler = handler;
  return this;
};

Bristleback.controller.Action.prototype.renderOnResponse = function(templateName, containerDiv, rootObjectName) {
  var templateInformation = Bristleback.controller.TemplateController.constructTemplateInformation(templateName, containerDiv, rootObjectName);

  this.incomingMessageHandler = function(actionMessage) {
    Bristleback.controller.TemplateController.render(templateInformation, actionMessage);
  };
  return this;
};

Bristleback.controller.Action.prototype.renderOnException = function(exceptionType, templateName, containerDiv, abortExceptionProcessing) {
  abortExceptionProcessing = abortExceptionProcessing !== false;
  var templateInformation = Bristleback.controller.TemplateController.constructTemplateInformation(templateName, containerDiv, "exception");

  this.exceptionHandler.setExceptionHandler(exceptionType, function(exceptionMessage) {
    Bristleback.controller.TemplateController.render(templateInformation, exceptionMessage);
    return abortExceptionProcessing;
  });
  return this;
};

Bristleback.controller.Action.prototype.renderOnDefaultException = function(templateName, containerDiv, abortExceptionProcessing) {
  abortExceptionProcessing = abortExceptionProcessing !== false;
  var templateInformation = Bristleback.controller.TemplateController.constructTemplateInformation(templateName, containerDiv, "exception");

  this.exceptionHandler.setDefaultExceptionHandler(function(exceptionMessage) {
    Bristleback.controller.TemplateController.render(templateInformation, exceptionMessage);
    return abortExceptionProcessing;
  });
  return this;
};

Bristleback.controller.Action.prototype.defaultProtocolExceptionHandlerFunction = function(exceptionMessage) {
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

//------------- DEFAULT CONTROLLERS

Bristleback.controller.controllers["system.controller.action"] = Bristleback.controller.ActionController;
