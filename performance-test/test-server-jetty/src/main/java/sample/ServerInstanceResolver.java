package sample;

import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.api.ServletServerInstanceResolver;
import pl.bristleback.server.bristle.app.BristlebackBootstrap;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.app.servlet.BristlebackServletInitParameters;
import pl.bristleback.server.bristle.conf.resolver.init.PojoConfigResolver;
import pl.bristleback.test.action.HelloWorldAction;
import pl.bristleback.test.action.HelloWorldClientAction;
import pl.bristleback.test.handlers.StandardConnectionStateListener;

public class ServerInstanceResolver implements ServletServerInstanceResolver {

  @Override
  public BristlebackServerInstance createServerInstance(BristlebackServletInitParameters initParameters) {
    PojoConfigResolver configurationResolver = new PojoConfigResolver();
    configurationResolver.setEngineName("system.engine.jetty.servlet");
    configurationResolver.setLoggingLevel("DEBUG");

    return prepareServerInstance(configurationResolver);
  }

  public BristlebackServerInstance createStandaloneServerInstance() {
    PojoConfigResolver configurationResolver = new PojoConfigResolver();
    configurationResolver.setEngineName("system.engine.jetty.servlet");
    configurationResolver.setLoggingLevel("DEBUG");

    return prepareServerInstance(configurationResolver);
  }

  private BristlebackServerInstance prepareServerInstance(InitialConfigurationResolver configurationResolver) {
    BristlebackBootstrap bootstrap = BristlebackBootstrap.init(configurationResolver);
    HelloWorldClientAction helloWorldClientAction = bootstrap.registerClientActionClass(new HelloWorldClientAction());
    HelloWorldAction helloWorldAction = new HelloWorldAction();
    helloWorldAction.setHelloWorldClientAction(helloWorldClientAction);

    bootstrap.registerActionClass(helloWorldAction);

    bootstrap.registerConnectionStateListener(new StandardConnectionStateListener());

    return bootstrap.createServerInstance();
  }
}
