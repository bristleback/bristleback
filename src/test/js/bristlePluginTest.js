TestCase("BristlePluginTestCase", {
  setUp : function() {
    connector = new jws.jWebSocketJSONClient();
  },

  testShouldConnectorBeCreated : function() {
    assertNotUndefined(connector);
    assertNotUndefined(connector.logon);
    assertNotUndefined(connector.sendToken);
    assertNotUndefined(connector.start);
  },

  testShouldConnectBeOverriden : function() {
    assertEquals("test", connector.connect());
  },

  testShouldAssertCorrectly : function() {
    expectAsserts(1);
    assertTrue(true);
    console.log("Tests are running correctly")
  }

});
