Bristleback.controller.TemplateController = function(){
  this.parsedTemplates= {}

};

Bristleback.controller.TemplateController.prototype.constructTemplateInformation = function (templateName, containerDiv, rootObjectName) {
  containerDiv = containerDiv ? containerDiv : "#" + templateName + "-div";

  var templateInformation = {};
  templateInformation.rootObjectName = rootObjectName;
  templateInformation.containerDiv = containerDiv;
  templateInformation.templateName = templateName;
  return templateInformation;
};

Bristleback.controller.TemplateController.prototype.containsTemplate = function (templateName) {
  return this.parsedTemplates[templateName] != undefined;
};

Bristleback.controller.TemplateController.prototype.getTemplate = function (templateName) {

  if (!this.containsTemplate(templateName)) {
    this.parsedTemplates[templateName] =
      Bristleback.controller.TemplateController.templateFramework.parseTemplate(templateName);
  }
  return this.parsedTemplates[templateName];
};

Bristleback.controller.TemplateController.prototype.render = function (templateInformation, object) {
  if (templateInformation.rootObjectName) {
    var data = {};
    data[templateInformation.rootObjectName] = object;
  } else {
    data = object;
  }

  var parsedTemplate = this.getTemplate(templateInformation.templateName);
  var result = Bristleback.controller.TemplateController.templateFramework.processTemplate(parsedTemplate, data);
  var idWithoutHash = templateInformation.containerDiv.substring(1);
  var container = document.getElementById(idWithoutHash);
  container.innerHTML = result;
};

Bristleback.controller.TemplateController.prototype.registerTemplateFramework = function (templateFramework) {
  Bristleback.controller.TemplateController.templateFramework = templateFramework;
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


