/**
 Template module

 @module Bristleback
 @submodule template
 @main Bristleback
 **/

/**
 * Template controller is responsible for registering template frameworks and storing, caching and rendering templates.
 * Before using template controller, template framework implementation must be registered via registerTemplateFramework() method.
 * @class TemplateController
 * @namespace Bristleback.template
 * @constructor
 */
Bristleback.template.TemplateController = function () {
  this.parsedTemplates = {}

};

Bristleback.template.TemplateController.prototype.constructTemplateInformation = function (templateName, containerDiv, rootObjectName) {
  containerDiv = containerDiv ? containerDiv : "#" + templateName + "-div";

  var templateInformation = {};
  templateInformation.rootObjectName = rootObjectName;
  templateInformation.containerDiv = containerDiv;
  templateInformation.templateName = templateName;
  return templateInformation;
};

/**
 * Checks if there is template with name given as parameter.
 * @method containsTemplate
 * @param templateName
 * @return {Boolean}
 */
Bristleback.template.TemplateController.prototype.containsTemplate = function (templateName) {
  return this.parsedTemplates[templateName] != undefined;
};

/**
 * Resolves template with name given as parameter.
 * If there is no parsed template cached in templates container, template framework is used to parse template.
 * @method getTemplate
 * @param {String} templateName
 * @return {Object} parsed template.
 */
Bristleback.template.TemplateController.prototype.getTemplate = function (templateName) {
  if (!this.containsTemplate(templateName)) {
    this.parsedTemplates[templateName] =
      this.templateFramework.parseTemplate(templateName);
  }
  return this.parsedTemplates[templateName];
};

/**
 * Renders template using template information object.
 * @method render
 * @param {Object} templateInformation information about template.
 * @param {Object} object data used by template.
 */
Bristleback.template.TemplateController.prototype.render = function (templateInformation, object) {
  if (templateInformation.rootObjectName) {
    var data = {};
    data[templateInformation.rootObjectName] = object;
  } else {
    data = object;
  }

  var parsedTemplate = this.getTemplate(templateInformation.templateName);
  var result = this.templateFramework.processTemplate(parsedTemplate, data);
  var idWithoutHash = templateInformation.containerDiv.substring(1);
  var container = document.getElementById(idWithoutHash);
  container.innerHTML = result;
};

/**
 * Registers template framework that will be used by template controller.
 * Template framework must provide 2 methods:
 * <ul>
 *   <li>parseTemplate(templateName)</li>
 *   <li>processTemplate(parsedTemplate, data)</li>
 * </ul>
 * Several template frameworks can be found in Bristleback.templateFrameworks object.
 * @method registerTemplateFramework
 * @param {Object} templateFramework
 */
Bristleback.template.TemplateController.prototype.registerTemplateFramework = function (templateFramework) {
  this.templateFramework = templateFramework;
};

Bristleback.templateFrameworks = {

  trimpath: {
    parseTemplate: function (templateName) {
      if (!document.getElementById(templateName)) {
        var logMsg = "Cannot find template with id: " + templateName;
        Bristleback.Console.log(logMsg);
        throw new Error(logMsg);
      }
      return TrimPath.parseDOMTemplate(templateName);
    },
    processTemplate: function (parsedTemplate, data) {
      return parsedTemplate.process(data);
    }
  },
  handlebars: {
    parseTemplate: function (templateName) {
      var templateContainer = document.getElementById(templateName);
      if (!templateContainer) {
        var logMsg = "Cannot find template with id: " + templateName;
        Bristleback.Console.log(logMsg);
        throw new Error(logMsg);
      }

      var source = templateContainer.innerHTML;
      return Handlebars.compile(source);
    },
    processTemplate: function (parsedTemplate, data) {
      return parsedTemplate(data);
    }
  }

};

Bristleback.templateController = new Bristleback.template.TemplateController();


