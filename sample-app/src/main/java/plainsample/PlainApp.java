package plainsample;

import pl.bristleback.server.bristle.app.BristlebackBootstrap;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import sample.action.HelloWorldAction;

public class PlainApp {

  public static void main(String[] args) {
    BristlebackBootstrap bootstrap = BristlebackBootstrap.init();
    bootstrap.registerActionClass(new HelloWorldAction());

    BristlebackServerInstance serverInstance = bootstrap.createServerInstance();

    serverInstance.startServer();
  }
}
