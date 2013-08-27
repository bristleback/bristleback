var BristleTest = {

  running: false,

  performTests: function (clientsNumber, messagesNumber) {
    if (BristleTest.running) {
      throw new Error("Tests are already running");
    }
    BristleTest.running = true;

    var testContext = {
      clientsNumber: clientsNumber,
      messagesNumber: messagesNumber,
      connectedClients: 0,
      receivedMessages: 0,
      clients: []
    };
    var config = BristleTest.createConfig(testContext);
    for (var i = 0; i < clientsNumber; i++) {
      var client = BristleTest.createClient(testContext, config);
      testContext.clients.push(client);
    }

    BristleTest.startTests(testContext);
  },

  createConfig: function (testContext) {
    return {
      serverUrl: "ws://127.0.0.1:8765/websocket",
      onOpen: function () {
        testContext.connectedClients++;
      },
      onClose: function () {
        testContext.connectedClients--;
      }
    }
  },

  createClient: function (testContext, config) {
    var client = Bristleback.newClient(config);
    var dataController = client.dataController;

    var helloWorld = dataController.getActionClass("HelloWorld");

    helloWorld.defineDefaultAction().setResponseHandler(function () {
      testContext.receivedMessages++;
    });

    return client;
  },

  startTests: function (testContext) {
    BristleTestView.displayTestStartInfo(testContext);
    var timeStart = new Date().getTime();
    for (var i = 0; i < testContext.clients.length; i++) {
      var client = testContext.clients[i];
      client.connect();
    }
    var intervalId = window.setInterval(function () {
      if (testContext.connectedClients == testContext.clientsNumber) {
        window.clearInterval(intervalId);
        var totalTime = new Date().getTime() - timeStart;
        BristleTestView.displayConnectionTestResultsInfo(testContext, totalTime);
        BristleTest.testMessageSending(testContext);
      }
    }, 10);
  },

  testMessageSending: function (testContext) {
    var timeStart = new Date().getTime();
    var clientIndex = 0;
    for (var i = 0; i < testContext.messagesNumber; i++) {
      testContext.clients[clientIndex].dataController.getActionClass("HelloWorld").executeDefault(BB.USER_CONTEXT, "Some name");
      clientIndex++;
      if (clientIndex == testContext.clientsNumber) {
        clientIndex = 0;
      }
    }
    var intervalId = window.setInterval(function () {
      BristleTestView.actualProgressInfo(testContext);
      if (testContext.receivedMessages == testContext.messagesNumber) {
        window.clearInterval(intervalId);
        var totalTime = new Date().getTime() - timeStart;
        BristleTestView.displayMessageSendingResultsInfo(testContext, totalTime);
        BristleTest.testConnectionClosing(testContext);
      }
    }, 10);
  },

  testConnectionClosing: function (testContext) {
    var timeStart = new Date().getTime();
    for (var i = 0; i < testContext.clients.length; i++) {
      var client = testContext.clients[i];
      client.disconnect();
    }
    var intervalId = window.setInterval(function () {
      if (testContext.connectedClients == 0) {
        window.clearInterval(intervalId);
        var totalTime = new Date().getTime() - timeStart;
        BristleTestView.displayDisconnectionTestsResultsInfo(testContext, totalTime);
        BristleTest.running = false;
      }
    }, 10);
  }
};


var BristleTestView = {

  displayTestStartInfo: function (testContext) {
    $('#numberOfClientsInfo').text(testContext.clientsNumber);
    $('#numberOfMessagesInfo').text(testContext.messagesNumber);
    $('#connectionTestsTime').text();
    $('#messagesTestTime').text();
    $('#messagesProgressInner').css("width", 0).text("");
  },

  displayConnectionTestResultsInfo: function (testContext, totalTime) {
    $('#connectionTestsTime').text("Opened " + testContext.clientsNumber + " connections in " + totalTime + " ms");
    $('#messagesProgress').show();
  },

  actualProgressInfo: function (testContext) {
    var progress = (testContext.receivedMessages / testContext.messagesNumber * 100).toFixed(0) + "%";
    $('#messagesProgressInner').css("width", progress).text(progress);
  },

  displayMessageSendingResultsInfo: function (testContext, totalTime) {
    var messagesPerSecond = (testContext.messagesNumber * 1000 / totalTime).toFixed(0);
    $('#messagesTestTime').html("Sent/received " + testContext.messagesNumber + " messages in " + totalTime + " ms." +
      "<br> (" + messagesPerSecond + " per second)");
    $('#messagesProgress').hide();
  },

  displayDisconnectionTestsResultsInfo: function (testContext, totalTime) {
    $('#disconnectionTestsTime').text("Closed " + testContext.clientsNumber + " connections in " + totalTime + " ms");
    $('#startTests').prop("disabled", false);
  }
};