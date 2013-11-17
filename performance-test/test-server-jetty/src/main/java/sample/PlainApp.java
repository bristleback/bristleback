package sample;

import pl.bristleback.server.bristle.app.BristlebackServerInstance;

public class PlainApp {

  public static void main(String[] args) {
    BristlebackServerInstance serverInstance = new ServerInstanceResolver().createStandaloneServerInstance();
    serverInstance.startServer();
  }
}