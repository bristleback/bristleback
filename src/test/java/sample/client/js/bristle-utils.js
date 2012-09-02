//------------- INIT NAMESPACE
var Bristleback = Bristleback || {};

Bristleback.controller = {};

Bristleback.serialization = {};

Bristleback.utils = {};

Bristleback.Console = {};

//------------- INIT CONSTANTS

Bristleback.LOCAL_HOSTNAME = "ws://" + ( self.location.hostname ? self.location.hostname : "127.0.0.1" ) + ":8765/websocket";

Bristleback.CONNECTOR = "cn";

//------------- UTILITY FUNCTIONS

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
 * @param txt text to escape characters
 */
Bristleback.utils.escapeHTML = function(txt) {
  var div = document.createElement('div');
  var text = document.createTextNode(txt);
  div.appendChild(text);
  return div.innerHTML;
};

//------------- CONSOLE

Bristleback.Console.enabled = false;

Bristleback.Console.consoleDiv = $("<textarea id='bristleConsole' style='font-size: 10pt; position: absolute; bottom: 0; left: 0; width:98%; height: 100px; border: solid 1px #dcdcdc; overflow-y: scroll; padding: 2px;'>Bristleback console v0.1\n</textarea>");

Bristleback.Console.log = function(msg) {
  if (Bristleback.Console.enabled) {
    var console = $("#bristleConsole");
    console.append(msg + "\n");
    console.scrollTop(console[0].scrollHeight - console.height());
  }
};

Bristleback.Console.enableConsole = function() {
  var consoleDiv = $("#bristleConsole");
  Bristleback.Console.enabled = true;
  if (consoleDiv.length == 0) {
    $('body').append(Bristleback.Console.consoleDiv);
  } else if (!consoleDiv.is(":visible")) {
    consoleDiv.show();
  }
};

Bristleback.Console.disableConsole = function() {
  Bristleback.Console.enabled = false;
  $("#bristleConsole").hide();
};