/**
 Utilities module

 @module Bristleback
 @submodule utils
 **/


//------------- INIT CONSTANTS

/**
 * Local host name with port 8765.
 *
 * @class LOCAL_HOSTNAME
 * @namespace Bristleback
 * @type String
 * @final
 **/
Bristleback.LOCAL_HOSTNAME = "ws://" + ( self.location.hostname ? self.location.hostname : "127.0.0.1" ) + ":8765/websocket";

/**
 * Bristleback connection placeholder
 *
 * @class CONNECTOR
 * @namespace Bristleback
 * @type String
 * @final
 * @default "cn"
 **/
Bristleback.CONNECTOR = "cn";

/**
 * @class utils
 * @namespace Bristleback
 * @static
 */
Bristleback.utils = {};

//------------- UTILITY FUNCTIONS

/**
 * Creates a deep copy of object.
 *
 * @method deepCopy
 * @static
 * @param p {Object} object that will be copied
 * @param c {Object} object that will receive state of first object
 **/
Bristleback.utils.deepCopy = function(p, c) {
  c = c || {};
  for (var i in p) {
    if (typeof p[i] === 'object') {
      c[i] = (p[i].constructor === Array) ? [] : {};
      Bristleback.utils.deepCopy(p[i], c[i]);
    } else {
      if (typeof p[i] !== 'function') {
        c[i] = p[i];
      }
    }
  }
  return c;
};

/**
 * This utility method iterates over all fields of given object and returns complete state of it in a "JSON" like way.
 * @method objectToString
 * @static
 * @param obj
 */
Bristleback.utils.objectToString = function(obj) {
  var objectAsString = "";
  var innerObject = "";
  var startMark = "{";
  var endMark = "}";
  for (var field in obj) {
    if (typeof obj[field] === 'object') {
      if (obj[field] instanceof Array) {
        startMark = "[";
        endMark = "]";
      }
      innerObject = Bristleback.utils.objectToString(obj[field]);
      if (innerObject != "") {
        objectAsString += field + " " + startMark + "\n";
        objectAsString += innerObject;
        objectAsString += endMark + "\n";
      }
    } else {
      if (!(obj[field] instanceof Function)) {
        objectAsString += field + ": " + obj[field] + "\n";
      }
    }
  }
  return objectAsString;
};

/**
 * Function used to prevent cross site scripting.
 * You should used it on every message from server that you want to insert into DOM.
 * @method escapeHTML
 * @static
 * @param txt text to escape characters
 */
Bristleback.utils.escapeHTML = function(txt) {
  var div = document.createElement('div');
  var text = document.createTextNode(txt);
  div.appendChild(text);
  return div.innerHTML;
};

//------------- CONSOLE
/**
 * @class Console
 * @namespace Bristleback
 * @static
 */
Bristleback.Console = {};

Bristleback.Console.enabled = false;

Bristleback.Console.consoleDiv = "<textarea id='bristleConsole' style='font-size: 10pt; position: absolute; bottom: 0; left: 0; width:98%; height: 100px; border: solid 1px #dcdcdc; overflow-y: scroll; padding: 2px;'>Bristleback console v0.1\n</textarea>";

/**
 * Writes given message in console dom element. If console is not enabled, nothing happens.
 *
 * @method log
 * @static
 * @param msg {String} log message
 **/
Bristleback.Console.log = function(msg) {
  if (Bristleback.Console.enabled) {
    var console = document.getElementById('bristleConsole');
    console.innerHTML += (msg + "\n");
    console.scrollTop = console.scrollHeight - console.clientHeight;
  }
};

/**
 * Enables the Bristleback logging console.
 * @method enableConsole
 * @static
 */
Bristleback.Console.enableConsole = function() {
  var consoleDiv = document.getElementById('bristleConsole');
  Bristleback.Console.enabled = true;
  if (!consoleDiv) {
    var body = document.getElementsByTagName('body')[0];
    body.innerHTML += Bristleback.Console.consoleDiv;
  } else if (consoleDiv.style.display == "none") {
    consoleDiv.style.display = "block";
  }
};

/**
 * Disables the Bristleback logging console.
 * @method disableConsole
 * @static
 */
Bristleback.Console.disableConsole = function() {
  var consoleDiv = document.getElementById('bristleConsole');
  Bristleback.Console.enabled = false;
  consoleDiv.style.display = "none";
};