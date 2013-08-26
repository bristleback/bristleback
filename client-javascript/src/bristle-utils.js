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
BB.LOCAL_HOSTNAME = "ws://" + ( self.location.hostname ? self.location.hostname : "127.0.0.1" ) + ":8765/websocket";

/**
 * Bristleback User Context action parameter placeholder
 *
 * @class USER_CONTEXT
 * @namespace Bristleback
 * @type String
 * @final
 * @default "uc"
 **/
BB.USER_CONTEXT = "uc";

/**
 * @class utils
 * @namespace Bristleback
 * @static
 */
BB.utils = {};

//------------- UTILITY FUNCTIONS

/**
 * Creates a deep copy of object.
 *
 * @method deepCopy
 * @static
 * @param p {Object} object that will be copied
 * @param c {Object} object that will receive state of first object
 **/
BB.utils.deepCopy = function(p, c) {
  c = c || {};
  for (var i in p) {
    if (typeof p[i] === 'object') {
      c[i] = (p[i].constructor === Array) ? [] : {};
      BB.utils.deepCopy(p[i], c[i]);
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
BB.utils.objectToString = function(obj) {
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
      innerObject = BB.utils.objectToString(obj[field]);
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
BB.utils.escapeHTML = function(txt) {
  var div = document.createElement('div');
  var text = document.createTextNode(txt);
  div.appendChild(text);
  return div.innerHTML;
};