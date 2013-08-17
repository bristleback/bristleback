package sample;

import pl.bristleback.server.bristle.app.BristlebackBootstrap;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;

public class PlainApp {

  public static void main(String[] args) {
    BristlebackBootstrap bootstrap = BristlebackBootstrap.init();

    BristlebackServerInstance serverInstance = bootstrap.createServerInstance();

    serverInstance.startServer();
  }
}
