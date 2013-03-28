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