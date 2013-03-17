/**
 Template module

 @module Bristleback
 @submodule template
 @main Bristleback
 **/

/**
 * Template controller is responsible for registering template frameworks and storing, caching and rendering templates.
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

Bristleback.template.TemplateController.prototype.containsTemplate = function (templateName) {
  return this.parsedTemplates[templateName] != undefined;
};

Bristleback.template.TemplateController.prototype.getTemplate = function (templateName) {

  if (!this.containsTemplate(templateName)) {
    this.parsedTemplates[templateName] =
      this.templateFramework.parseTemplate(templateName);
  }
  return this.parsedTemplates[templateName];
};

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


