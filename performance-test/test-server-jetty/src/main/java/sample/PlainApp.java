package sample;

import pl.bristleback.server.bristle.app.BristlebackBootstrap;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.conf.resolver.init.PojoConfigResolver;
import pl.bristleback.test.action.HelloWorldAction;
import pl.bristleback.test.action.HelloWorldClientAction;
import pl.bristleback.test.handlers.StandardConnectionStateListener;

public class PlainApp {

  public static void main(String[] args) {
    PojoConfigResolver pojoConfigResolver = new PojoConfigResolver();
    pojoConfigResolver.setLoggingLevel("DEBUG");
    BristlebackBootstrap bootstrap = BristlebackBootstrap.init(pojoConfigResolver);
    HelloWorldClientAction helloWorldClientAction = bootstrap.registerClientActionClass(new HelloWorldClientAction());
    HelloWorldAction helloWorldAction = new HelloWorldAction();
    helloWorldAction.setHelloWorldClientAction(helloWorldClientAction);

    bootstrap.registerActionClass(helloWorldAction);

    bootstrap.registerConnectionStateListener(new StandardConnectionStateListener());

    BristlebackServerInstance serverInstance = bootstrap.createServerInstance();

    serverInstance.startServer();
  }
}