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
        Bristleback.Console.log("User " + username + " has been successfully authenticated.")
      }
    },
    logout: function (logoutReason) {
      if (authentication.logoutCallback) {
        authentication.logoutCallback(logoutReason);
      } else {
        Bristleback.Console.log("User has been logged out (logout reason: " + logoutReason + ").")
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

Bristleback.auth.SystemAuthentication.prototype.defaultSecurityExceptionHandler = function (exception) {
  var exceptionMessage = "Unexpected " + exception.exceptionType + " exception occurred for user \"" + exception.content.username + "\".";
  Bristleback.Console.log("[ERROR] " + exceptionMessage);
  throw new Error(exceptionMessage);
};

Bristleback.auth.SystemAuthentication.prototype.defaultIncorrectPasswordHandler = function (exception) {
  var exceptionMessage = "Incorrect username or password, username provided: \"" + exception.content.username + "\".";
  Bristleback.Console.log("[ERROR] " + exceptionMessage);
  throw new Error(exceptionMessage);
};

Bristleback.auth.SystemAuthentication.prototype.defaultAuthorizationExceptionHandler = function (exception) {
  var exceptionMessage = "User \"" + exception.content.username + "\" tried to invoke action without required authority: \"" + exception.content.authority + "\".";
  Bristleback.Console.log("[ERROR] " + exceptionMessage);
  throw new Error(exceptionMessage);
};

Bristleback.auth.SystemAuthentication.prototype.authenticate = function () {
  this.invokeAction(this.defaultAuthenticationAction, arguments);
};

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

Bristleback.auth.SystemAuthentication.prototype.setAsDefaultAuthenticationAction = function (action) {
  this.defaultAuthenticationAction = action;
};

Bristleback.auth.SystemAuthentication.prototype.setAsDefaultLogoutAction = function (action) {
  this.defaultLogoutAction = action;
};

Bristleback.auth.SystemAuthentication.prototype.setAuthenticationSuccessCallback = function (authenticationSuccessCallback) {
  this.authenticationSuccessCallback = authenticationSuccessCallback;
};

Bristleback.auth.SystemAuthentication.prototype.setLogoutCallback = function (logoutCallback) {
  this.logoutCallback = logoutCallback;
};