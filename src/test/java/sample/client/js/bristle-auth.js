Bristleback.auth.SystemAuthentication = function (dataController) {
  var defaultAuthenticationActionClass = dataController.getActionClass("BristleSystemUserAuthentication");
  this.setAsDefaultAuthenticationAction(defaultAuthenticationActionClass.defineAction("authenticate"));

  var logoutActionClass = dataController.getActionClass("BristleSystemUserLogout");
  this.setAsDefaultLogoutAction(logoutActionClass.defineAction("logout"));

  var authentication = this;
  var systemAuthClientActionClass = {
    logout: function () {
      if (authentication.logoutCallback) {
        authentication.logoutCallback();
      } else {
        Bristleback.Console.log("User has been logged out.")
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

Bristleback.auth.SystemAuthentication.prototype.setLogoutCallback = function (logoutCallback) {
  this.logoutCallback = logoutCallback;
};