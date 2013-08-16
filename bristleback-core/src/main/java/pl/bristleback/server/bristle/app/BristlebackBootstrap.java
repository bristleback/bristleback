package pl.bristleback.server.bristle.app;

import pl.bristleback.server.bristle.api.ApplicationComponentsResolver;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.conf.resolver.ServerInstanceResolver;
import pl.bristleback.server.bristle.conf.resolver.plain.PlainJavaApplicationComponentsResolver;

public final class BristlebackBootstrap {

  private InitialConfigurationResolver initialConfigurationResolver;
  private ApplicationComponentsResolver componentsResolver;

  private BristlebackBootstrap() {
    this.componentsResolver = new PlainJavaApplicationComponentsResolver();
  }

  private BristlebackBootstrap(InitialConfigurationResolver initialConfigurationResolver) {
    this();
    this.initialConfigurationResolver = initialConfigurationResolver;
  }

  public static BristlebackBootstrap init() {
    return new BristlebackBootstrap();
  }

  public static BristlebackBootstrap init(InitialConfigurationResolver initialConfigurationResolver) {
    return new BristlebackBootstrap(initialConfigurationResolver);
  }

  public BristlebackServerInstance createServerInstance() {
    ServerInstanceResolver serverInstanceResolver = new ServerInstanceResolver(initialConfigurationResolver, componentsResolver);
    return serverInstanceResolver.resolverServerInstance();
  }
}
