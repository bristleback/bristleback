var Bristleback=Bristleback||{};Bristleback.controller={};Bristleback.serialization={};Bristleback.template={};Bristleback.auth={};Bristleback.LOCAL_HOSTNAME="ws://"+(self.location.hostname?self.location.hostname:"127.0.0.1")+":8765/websocket";Bristleback.CONNECTOR="cn";Bristleback.utils={};Bristleback.utils.deepCopy=function(a,b){b=b||{};for(var c in a)if(typeof a[c]==="object"){b[c]=a[c].constructor===Array?[]:{};Bristleback.utils.deepCopy(a[c],b[c])}else if(typeof a[c]!=="function")b[c]=a[c];return b};
Bristleback.utils.objectToString=function(a){var b="",c="",d="{",e="}",f;for(f in a)if(typeof a[f]==="object"){if(a[f]instanceof Array){d="[";e="]"}c=Bristleback.utils.objectToString(a[f]);if(c!=""){b+=f+" "+d+"\n";b+=c;b+=e+"\n"}}else a[f]instanceof Function||(b+=f+": "+a[f]+"\n");return b};Bristleback.utils.escapeHTML=function(a){var b=document.createElement("div");a=document.createTextNode(a);b.appendChild(a);return b.innerHTML};Bristleback.Console={};Bristleback.Console.enabled=false;
Bristleback.Console.consoleDiv="<textarea id='bristleConsole' style='font-size: 10pt; position: absolute; bottom: 0; left: 0; width:98%; height: 100px; border: solid 1px #dcdcdc; overflow-y: scroll; padding: 2px;'>Bristleback console v0.1\n</textarea>";Bristleback.Console.log=function(a){if(Bristleback.Console.enabled){var b=document.getElementById("bristleConsole");b.innerHTML+=a+"\n";b.scrollTop=b.scrollHeight-b.clientHeight}};
Bristleback.Console.enableConsole=function(){var a=document.getElementById("bristleConsole");Bristleback.Console.enabled=true;if(a){if(a.style.display=="none")a.style.display="block"}else document.getElementsByTagName("body")[0].innerHTML+=Bristleback.Console.consoleDiv};Bristleback.Console.disableConsole=function(){var a=document.getElementById("bristleConsole");Bristleback.Console.enabled=false;a.style.display="none"};Bristleback.template.TemplateController=function(){this.parsedTemplates={}};Bristleback.template.TemplateController.prototype.constructTemplateInformation=function(a,b,c){b=b?b:"#"+a+"-div";var d={};d.rootObjectName=c;d.containerDiv=b;d.templateName=a;return d};Bristleback.template.TemplateController.prototype.containsTemplate=function(a){return this.parsedTemplates[a]!=undefined};
Bristleback.template.TemplateController.prototype.getTemplate=function(a){this.containsTemplate(a)||(this.parsedTemplates[a]=this.templateFramework.parseTemplate(a));return this.parsedTemplates[a]};Bristleback.template.TemplateController.prototype.render=function(a,b){if(a.rootObjectName){var c={};c[a.rootObjectName]=b}else c=b;c=this.templateFramework.processTemplate(this.getTemplate(a.templateName),c);var d=a.containerDiv.substring(1);document.getElementById(d).innerHTML=c};
Bristleback.template.TemplateController.prototype.registerTemplateFramework=function(a){this.templateFramework=a};
Bristleback.templateFrameworks={trimpath:{parseTemplate:function(a){if(!document.getElementById(a)){a="Cannot find template with id: "+a;Bristleback.Console.log(a);throw Error(a);}return TrimPath.parseDOMTemplate(a)},processTemplate:function(a,b){return a.process(b)}},handlebars:{parseTemplate:function(a){var b=document.getElementById(a);if(!b){a="Cannot find template with id: "+a;Bristleback.Console.log(a);throw Error(a);}return Handlebars.compile(b.innerHTML)},processTemplate:function(a,b){return a(b)}}};
Bristleback.templateController=new Bristleback.template.TemplateController;Bristleback.auth.SystemAuthentication=function(a){this.setAsDefaultAuthenticationAction(a.getActionClass("BristleSystemUserAuthentication").defineAction("authenticate"));this.setAsDefaultLogoutAction(a.getActionClass("BristleSystemUserLogout").defineAction("logout"));var b=this;a.registerClientActionClass("SystemAuth",{authenticationSuccess:function(c){b.authenticationSuccessCallback?b.authenticationSuccessCallback(c):Bristleback.Console.log("User "+c+" has been successfully authenticated.")},logout:function(c){b.logoutCallback?
b.logoutCallback(c):Bristleback.Console.log("User has been logged out (logout reason: "+c+").")}});a.exceptionHandler.setExceptionHandler("BristleSecurityException",this.defaultSecurityExceptionHandler);a.exceptionHandler.setExceptionHandler("IncorrectUsernameOrPasswordException",this.defaultIncorrectPasswordHandler);a.exceptionHandler.setExceptionHandler("InactiveUserException",this.defaultSecurityExceptionHandler);a.exceptionHandler.setExceptionHandler("UserAlreadyAuthenticatedException",this.defaultSecurityExceptionHandler);
a.exceptionHandler.setExceptionHandler("UserNotAuthenticatedException",this.defaultSecurityExceptionHandler);a.exceptionHandler.setExceptionHandler("AuthorizationException",this.defaultAuthorizationExceptionHandler)};Bristleback.auth.SystemAuthentication.prototype.defaultSecurityExceptionHandler=function(a){a="Unexpected "+a.exceptionType+" security exception occurred"+(a.content.username?' for user "'+a.content.username+'".':".");Bristleback.Console.log("[ERROR] "+a);throw Error(a);};
Bristleback.auth.SystemAuthentication.prototype.defaultIncorrectPasswordHandler=function(a){a='Incorrect username or password, username provided: "'+a.content.username+'".';Bristleback.Console.log("[ERROR] "+a);throw Error(a);};Bristleback.auth.SystemAuthentication.prototype.defaultAuthorizationExceptionHandler=function(a){a='User "'+a.content.username+'" tried to invoke action without required authority: "'+a.content.authority+'".';Bristleback.Console.log("[ERROR] "+a);throw Error(a);};
Bristleback.auth.SystemAuthentication.prototype.authenticate=function(){this.invokeAction(this.defaultAuthenticationAction,arguments)};Bristleback.auth.SystemAuthentication.prototype.logout=function(){this.invokeAction(this.defaultLogoutAction,arguments)};Bristleback.auth.SystemAuthentication.prototype.invokeAction=function(a,b){for(var c=[],d=b.length,e=0;e<d;e++)c[e]=b[e];a.actionClass[a.name].apply(a.actionClass,c)};
Bristleback.auth.SystemAuthentication.prototype.setAsDefaultAuthenticationAction=function(a){this.defaultAuthenticationAction=a};Bristleback.auth.SystemAuthentication.prototype.setAsDefaultLogoutAction=function(a){this.defaultLogoutAction=a};Bristleback.auth.SystemAuthentication.prototype.setAuthenticationSuccessCallback=function(a){this.authenticationSuccessCallback=a};Bristleback.auth.SystemAuthentication.prototype.setLogoutCallback=function(a){this.logoutCallback=a};Bristleback.controller.controllers={};
Bristleback.controller.ActionMessage=function(a,b){var c=b.name.split(":"),d=c[0].split("."),e=d[0];d=d[1]?d[1]:"";this.actionClass=b.id?a.actionClasses[e]:a.clientActionClasses[e];if(this.actionClass==undefined){c='[ERROR] Cannot find a client action class "'+e+'"';Bristleback.Console.log(c);throw Error(c);}this.action=this.actionClass.actions[d];if(this.action==undefined){c="[ERROR] Cannot find action "+(d?'"'+d+'"':"default action ")+' in action class "'+e+'"';Bristleback.Console.log(c);throw Error(c);
}this.callback=a.callbacks[b.id];this.content=b.payload;this.exceptionType=c.length>1?this.content.type:undefined};Bristleback.controller.ActionExceptionHandler=function(){this.defaultRenderingHandler=this.defaultExceptionHandler=undefined;this.exceptionHandlers={};this.renderingHandlers={}};Bristleback.controller.ActionExceptionHandler.prototype.setDefaultExceptionHandler=function(a){this.defaultExceptionHandler=a;return this};
Bristleback.controller.ActionExceptionHandler.prototype.setExceptionHandler=function(a,b){this.exceptionHandlers[a]=b;return this};Bristleback.controller.ActionExceptionHandler.prototype.renderOnException=function(a,b,c){var d=Bristleback.templateController.constructTemplateInformation(b,c,"exception");this.renderingHandlers[a]=function(e){Bristleback.templateController.render(d,e)};return this};
Bristleback.controller.ActionExceptionHandler.prototype.renderOnDefaultException=function(a,b){var c=Bristleback.templateController.constructTemplateInformation(a,b,"exception");this.defaultRenderingHandler=function(d){Bristleback.templateController.render(c,d)};return this};Bristleback.controller.ActionExceptionHandler.prototype.handleException=function(a){var b=this.exceptionHandlers[a.exceptionType],c=this.renderingHandlers[a.exceptionType],d=false;if(b)d=b(a);if(c){d=true;c(a)}return d||this.handleDefault(a)};
Bristleback.controller.ActionExceptionHandler.prototype.handleDefault=function(a){var b=false;if(this.defaultExceptionHandler)b=this.defaultExceptionHandler(a);if(this.defaultRenderingHandler){b=true;this.defaultRenderingHandler(a)}return b};Bristleback.controller.ActionCallback=function(a){this.responseHandler=a;this.exceptionHandler=new Bristleback.controller.ActionExceptionHandler};Bristleback.controller.ActionCallback.prototype.handleResponse=function(a){return this.responseHandler(a)};
Bristleback.controller.ActionCallback.prototype.canHandleResponse=function(){return this.responseHandler!=undefined};Bristleback.controller.ActionController=function(){this.client=undefined;this.lastId=1;this.actionClasses={};this.clientActionClasses={};this.callbacks={};this.exceptionHandler=new Bristleback.controller.ActionExceptionHandler;this.exceptionHandler.setDefaultExceptionHandler(this.defaultHandlerFunction);this.authentication=new Bristleback.auth.SystemAuthentication(this)};
Bristleback.controller.ActionController.prototype.onMessage=function(a){a=new Bristleback.controller.ActionMessage(this,a);a.exceptionType?this.onExceptionMessage(a):a.actionClass.onMessage(a)};Bristleback.controller.ActionController.prototype.sendMessage=function(a,b,c){var d=this.lastId++;this.client.sendMessage({name:b?a+"."+b:a,payload:c,id:d});return d};
Bristleback.controller.ActionController.prototype.getActionClass=function(a){var b=this.actionClasses[a];if(b===undefined){b=new Bristleback.controller.ActionClass(a,this);this.actionClasses[a]=b}return b};Bristleback.controller.ActionController.prototype.onExceptionMessage=function(a){return a.callback&&a.callback.exceptionHandler.handleException(a)||a.action.exceptionHandler.handleException(a)||a.actionClass.exceptionHandler.handleException(a)||this.exceptionHandler.handleException(a)};
Bristleback.controller.ActionController.prototype.defaultHandlerFunction=function(a){a="["+(a.action.name?"Action "+a.actionClass.name+"."+a.action.name+"()":"Default action of class "+a.actionClass.name)+'] returned with exception of type "'+a.exceptionType+'" and detail message "'+Bristleback.utils.objectToString(a.content)+'"';Bristleback.Console.log("[ERROR] "+a);throw Error(a);};
Bristleback.controller.ActionController.prototype.registerClientActionClass=function(a,b){this.clientActionClasses[a]=new Bristleback.controller.ClientActionClass(a,b)};Bristleback.controller.ActionClass=function(a,b){this.actionController=b;this.name=a;this.actions={};this.exceptionHandler=new Bristleback.controller.ActionExceptionHandler;this.exceptionHandler.setExceptionHandler("BrokenActionProtocolException",this.defaultProtocolExceptionHandlerFunction)};
Bristleback.controller.ActionClass.prototype.defineDefaultAction=function(){return this.defineAction("")};Bristleback.controller.ActionClass.prototype.defineAction=function(a){if(this[a]!=undefined)throw Error("Action "+a+" already defined for action class "+this.name);this.actions[a]=new Bristleback.controller.Action(a,this);this[a]=function(){this.doSendMessage(this.actions[a],arguments)};return this.actions[a]};
Bristleback.controller.ActionClass.prototype.executeDefault=function(){this.doSendMessage(this.actions[""],arguments)};Bristleback.controller.ActionClass.prototype.getAction=function(a){return this.actions[a]};Bristleback.controller.ActionClass.prototype.getDefaultAction=function(){return this.actions[""]};
Bristleback.controller.ActionClass.prototype.doSendMessage=function(a,b){for(var c=[],d=b.length,e=0;e<d;e++)c[e]=b[e];d=c.length==0?undefined:c[c.length-1];if(d!=undefined)if(d instanceof Function){c.pop();var f=new Bristleback.controller.ActionCallback(d)}else if(d instanceof Bristleback.controller.ActionCallback)f=c.pop();c=this.actionController.sendMessage(this.name,a.name,c);if(f!=undefined)this.actionController.callbacks[c]=f};
Bristleback.controller.ActionClass.prototype.onMessage=function(a){if(a.callback!=undefined){this.actionController.callbacks[a.id]=undefined;if(a.callback.canHandleResponse()){a.callback.handleResponse(a.content);return}}this.runHandlers(a)};
Bristleback.controller.ActionClass.prototype.runHandlers=function(a){var b=a.action;if(b.responseHandler==undefined){a="["+(b.name?"Action "+this.name+"."+b.name+"()":"Default action of "+this.name)+"] Cannot find response handler for incoming action";Bristleback.Console.log("[ERROR] "+a);throw Error(a);}else b.responseHandler(a.content);b.renderingHandler&&b.renderingHandler(a.content)};
Bristleback.controller.ActionClass.prototype.defaultProtocolExceptionHandlerFunction=function(a){if(a.content=="NO_ACTION_CLASS_FOUND"){a='Cannot find action class with name "'+a.actionClass.name+'"';Bristleback.Console.log("[ERROR] "+a);throw Error(a);}else return false};Bristleback.controller.Action=function(a,b){this.name=a;this.actionClass=b;this.exceptionHandler=new Bristleback.controller.ActionExceptionHandler;this.exceptionHandler.setExceptionHandler("BrokenActionProtocolException",this.defaultProtocolExceptionHandlerFunction)};
Bristleback.controller.Action.prototype.setResponseHandler=function(a){this.responseHandler=a;return this};Bristleback.controller.Action.prototype.renderOnResponse=function(a,b,c){var d=Bristleback.templateController.constructTemplateInformation(a,b,c);this.renderingHandler=function(e){Bristleback.templateController.render(d,e)};return this};
Bristleback.controller.Action.prototype.defaultProtocolExceptionHandlerFunction=function(a){var b=a.content;if(b=="NO_DEFAULT_ACTION_FOUND")a='Cannot find default action in action class with name "'+a.actionClass.name+'"';else if(b=="NO_ACTION_FOUND")a='Cannot find action "'+a.action.name+'" in action class with name "'+a.actionClass.name+'"';else return false;Bristleback.Console.log("[ERROR] "+a);throw Error(a);};Bristleback.controller.ClientActionClass=function(a,b){this.name=a;this.actions=b};
Bristleback.controller.ClientActionClass.prototype.onMessage=function(a){var b=[],c=true,d=0;if(a.content!=undefined&&a.content!=null)for(;c;){var e=a.content["p"+d];if(e!=undefined){b[d]=e;d++}else c=false}if(b.length==0)b[0]=a.content;a.action.apply(a.actionClass,b)};Bristleback.controller.controllers["system.controller.action"]=Bristleback.controller.ActionController;Bristleback.serialization.serializationEngines={};Bristleback.serialization.setSerializationEngine=function(a,b){Bristleback.serialization.serializationEngines[a]=b};Bristleback.serialization.JsonEngine=function(){};Bristleback.serialization.JsonEngine.prototype.serialize=function(a){return JSON.stringify(a)};Bristleback.serialization.JsonEngine.prototype.deserialize=function(a){return JSON.parse(a)};Bristleback.serialization.serializationEngines.json=Bristleback.serialization.JsonEngine;Bristleback.Client=function(a){this.connection=undefined;a=a||{};a.serverUrl=a.serverUrl||Bristleback.LOCAL_HOSTNAME;a.serializationEngine=a.serializationEngine||"json";a.dataController=a.dataController||"system.controller.action";a.timeout=a.timeout||36E4;a.developmentConsole=a.developmentConsole||false;this.dataController=new Bristleback.controller.controllers[a.dataController];this.dataController.client=this;this.serializationEngine=new Bristleback.serialization.serializationEngines[a.serializationEngine];
a.OnOpen=a.OnOpen||function(){Bristleback.Console.log("Connected to "+a.serverUrl);alert("connected")};a.OnClose=a.OnClose||function(){Bristleback.Console.log("Disconnected from "+a.serverUrl);alert("disconnected")};var b=this;a.OnMessage=a.OnMessage||function(c){c=b.serializationEngine.deserialize(c.data);b.dataController.onMessage(c)};this.configuration=a};Bristleback.Client.prototype.getConnectionState=function(){if(!this.connection)return WebSocket.CLOSED;return this.connection.readyState};
Bristleback.Client.prototype.isConnected=function(){return this.getConnectionState()==WebSocket.OPEN};Bristleback.Client.prototype.isDisconnected=function(){return this.getConnectionState()==WebSocket.CLOSED};
Bristleback.Client.prototype.connect=function(){if(!this.isDisconnected()){Bristleback.Console.log("Connection is not closed, cannot establish another connection.");throw Error("Connection is not closed, cannot establish another connection.");}this.connection=new WebSocket(this.configuration.serverUrl,this.configuration.dataController);this.connection.onopen=this.configuration.OnOpen;this.connection.onmessage=this.configuration.OnMessage;this.connection.onclose=this.configuration.OnClose};
Bristleback.Client.prototype.sendMessage=function(a){if(!this.isConnected()){Bristleback.Console.log("Cannot send a message, connection is not open.");throw Error("Cannot send a message, connection is not open.");}this.connection.send(this.serializationEngine.serialize(a))};
Bristleback.Client.prototype.disconnect=function(){if(!this.isConnected()){var a="Connection is not open, it may be already closed or in closing state. Actual state: "+this.getConnectionState();Bristleback.Console.log(a);throw Error(a);}this.connection.close()};Bristleback.newClient=function(a){return new Bristleback.Client(a)};