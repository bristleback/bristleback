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
  this.parsedTemplates = {};

  /**
   Rendering modes, by default there are 3 modes available: "replace", "prepend" and "append".
   By default, "replace" mode is being applied. Replacing mode sets inner html content of the container.

   @property renderingModes
   @type Object
   **/
  this.renderingModes = Bristleback.template.builtInRenderingModes;
};

Bristleback.template.TemplateController.prototype.constructTemplateInformation = function (templateName, containerId, rootObjectName, renderingMode) {
  containerId = containerId ? containerId : "#" + templateName + "-div";
  renderingMode = renderingMode ? renderingMode : "replace";
  if (!this.renderingModes[renderingMode]) {
    throw new Error("Cannot find rendering mode with name: " + renderingMode);
  }

  var templateInformation = {};
  templateInformation.rootObjectName = rootObjectName;
  templateInformation.containerId = containerId;
  templateInformation.templateName = templateName;
  templateInformation.displayTemplate = this.renderingModes[renderingMode];
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
  templateInformation.displayTemplate(result, templateInformation);
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


Bristleback.template.builtInRenderingModes = {
  replace: function (result, templateInformation) {
    var idWithoutHash = templateInformation.containerId.substring(1);
    var container = document.getElementById(idWithoutHash);
    container.innerHTML = result;
  },
  append: function (result, templateInformation) {
    var idWithoutHash = templateInformation.containerId.substring(1);
    var container = document.getElementById(idWithoutHash);
    container.innerHTML = container.innerHTML + result;
  },
  prepend: function (result, templateInformation) {
    var idWithoutHash = templateInformation.containerId.substring(1);
    var container = document.getElementById(idWithoutHash);
    container.innerHTML = result + container.innerHTML;
  }
};

Bristleback.templateFrameworks = {

  trimpath: {
    parseTemplate: function (templateName) {
      if (!document.getElementById(templateName)) {
        throw new Error("Cannot find template with id: " + templateName);
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
        throw new Error("Cannot find template with id: " + templateName);
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


