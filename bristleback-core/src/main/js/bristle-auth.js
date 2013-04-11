/**
 Authentication module

 @module Bristleback
 @submodule auth
 @main Bristleback
 **/

/**
 * Creates a new instance of System Authentication controller, which is a part of Action controller.
 * Authentication controller provides methods for logging in and logging out.
 * It also allow user to handle various events and authentication/authorization exceptions.
 * This controller provides few built in handlers, all of them can be overridden by application creator.
 * Note that this is a first, beta version of the authentication controller,
 * future version will probably bring more sophisticated and more general solution,
 * that could be used also with Spring Security.
 * @param {Object} dataController data controller.
 * @class SystemAuthentication
 * @namespace Bristleback.auth
 * @constructor
 */
Bristleback.auth.SystemAuthentication = function (dataController) {
  var defaultAuthenticationActionClass = dataController.getActionClass("BristleSystemUserAuthentication");
  this.setAsDefaultAuthenticationAction(defaultAuthenticationActionClass.defineAction("authenticate"));

  var logoutActionClass = dataController.getActionClass("BristleSystemUserLogout");
  this.setAsDefaultLogoutAction(logoutActionClass.defineAction("logout"));

  var authentication = this;
  var systemAuthClientActionClass = {
    authenticationSuccess: function (username) {
      if (authentication.authenticationSuccessCallback) {
        authentication.authenticationSuccessCallback(username);
      } else {
        Bristleback.Console.log("User '" + username + "' has been successfully authenticated.")
      }
    },
    logout: function (logoutMessage) {
      if (authentication.logoutCallback) {
        authentication.logoutCallback(logoutMessage);
      } else {
        Bristleback.Console.log("User '" + logoutMessage.username + "' has been logged out (logout reason: " + logoutMessage.logoutReason + ").")
      }
    }
  };
  dataController.registerClientActionClass("SystemAuth", systemAuthClientActionClass);


  dataController.exceptionHandler.setExceptionHandler("BristleSecurityException", this.defaultSecurityExceptionHandler);
  dataController.exceptionHandler.setExceptionHandler("IncorrectUsernameOrPasswordException", this.defaultIncorrectPasswordHandler);
  dataController.exceptionHandler.setExceptionHandler("InactiveUserException", this.defaultSecurityExceptionHandler);
  dataController.exceptionHandler.setExceptionHandler("UserAlreadyAuthenticatedException", this.defaultSecurityExceptionHandler);
  dataController.exceptionHandler.setExceptionHandler("UserNotAuthenticatedException", this.defaultSecurityExceptionHandler);
  dataController.exceptionHandler.setExceptionHandler("AuthorizationException", this.defaultAuthorizationExceptionHandler);
};

/**
 * This is a default handler for following exception types:
 * <ul>
 *   <li>BristleSecurityException</li>
 *   <li>InactiveUserException</li>
 *   <li>UserAlreadyAuthenticatedException</li>
 *   <li>UserNotAuthenticatedException</li>
 * </ul>
 * @method defaultSecurityExceptionHandler
 * @param {Object} exception exception containing content and meta information, like exception type or action execution phase.
 */
Bristleback.auth.SystemAuthentication.prototype.defaultSecurityExceptionHandler = function (exception) {
  var usernameMessageSuffix = exception.content.username ? " for user '" + exception.content.username + "'." : ".";
  var exceptionMessage = "Unexpected " + exception.exceptionType + " security exception occurred" + usernameMessageSuffix;
  Bristleback.Console.log("[ERROR] " + exceptionMessage);
  throw new Error(exceptionMessage);
};

/**
 * This is a default handler for incorrect username or password exception (IncorrectUsernameOrPasswordException).
 * @method defaultIncorrectPasswordHandler
 * @param {Object} exception exception containing content and meta information, like exception type or action execution phase.
 */
Bristleback.auth.SystemAuthentication.prototype.defaultIncorrectPasswordHandler = function (exception) {
  var exceptionMessage = "Incorrect username or password, username provided: '" + exception.content.username + "'.";
  Bristleback.Console.log("[ERROR] " + exceptionMessage);
  throw new Error(exceptionMessage);
};

/**
 * This is a default handler for incorrect username or password exception (IncorrectUsernameOrPasswordException).
 * @method defaultIncorrectPasswordHandler
 * @param {Object} exception exception containing content and meta information, like exception type or action execution phase.
 */
Bristleback.auth.SystemAuthentication.prototype.defaultAuthorizationExceptionHandler = function (exception) {
  var exceptionMessage = "User '" + exception.content.username + "' tried to invoke action without required authority: '" + exception.content.authority + "'.";
  Bristleback.Console.log("[ERROR] " + exceptionMessage);
  throw new Error(exceptionMessage);
};

/**
 * Invokes default authentication action. Arguments of this method passed by user are further transferred to authentication action.
 * System authentication system provides built in default authentication action. To set custom action as the default,
 * use {{#crossLink "Bristleback.auth.SystemAuthentication/setAsDefaultAuthenticationAction"}}{{/crossLink}} method.
 * @method authenticate
 */
Bristleback.auth.SystemAuthentication.prototype.authenticate = function () {
  this.invokeAction(this.defaultAuthenticationAction, arguments);
};

/**
 * Invokes default logout action. Arguments of this method passed by user are further transferred to logout action.
 * System authentication system provides built in default logout action. To set custom action as the default,
 * use {{#crossLink "Bristleback.auth.SystemAuthentication/setAsDefaultLogoutAction"}}{{/crossLink}} method.
 * @method logout
 */
Bristleback.auth.SystemAuthentication.prototype.logout = function () {
  this.invokeAction(this.defaultLogoutAction, arguments);
};

Bristleback.auth.SystemAuthentication.prototype.invokeAction = function (action, argumentsList) {
  var correctParameters = [];
  var parametersLength = argumentsList.length;
  for (var i = 0; i < parametersLength; i++) {
    correctParameters[i] = argumentsList[i];
  }
  action.actionClass[action.name].apply(action.actionClass, correctParameters);
};

/**
 * Sets given action as the default authentication action invoked when calling
 * {{#crossLink "Bristleback.auth.SystemAuthentication/authenticate"}}{{/crossLink}} method.
 * @method setAsDefaultAuthenticationAction
 * @param {Object} action object created by calling {{#crossLink "Bristleback.controller.ActionClass/defineAction"}}{{/crossLink}} method.
 */
Bristleback.auth.SystemAuthentication.prototype.setAsDefaultAuthenticationAction = function (action) {
  this.defaultAuthenticationAction = action;
};

/**
 * Sets given action as the default authentication action invoked when calling
 * {{#crossLink "Bristleback.auth.SystemAuthentication/logout"}}{{/crossLink}} method.
 * @method setAsDefaultLogoutAction
 * @param {Object} action object created by calling {{#crossLink "Bristleback.controller.ActionClass/defineAction"}}{{/crossLink}} method.
 */
Bristleback.auth.SystemAuthentication.prototype.setAsDefaultLogoutAction = function (action) {
  this.defaultLogoutAction = action;
};

/**
 * Sets authentication success callback, invoked when system sends a message about successful authentication.
 *
 * @method setAuthenticationSuccessCallback
 * @param {Function} authenticationSuccessCallback function that takes one String parameter: username.
 */
Bristleback.auth.SystemAuthentication.prototype.setAuthenticationSuccessCallback = function (authenticationSuccessCallback) {
  this.authenticationSuccessCallback = authenticationSuccessCallback;
};

/**
 * Sets logout callback, invoked when system sends a message about finishing logging out process.
 *
 * @method setLogoutCallback
 * @param {Function} logoutCallback function that takes one Object parameter: logoutMessage,
 * containing fields: username and logoutReason.
 */
Bristleback.auth.SystemAuthentication.prototype.setLogoutCallback = function (logoutCallback) {
  this.logoutCallback = logoutCallback;
};