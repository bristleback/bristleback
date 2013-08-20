package pl.bristleback.server.bristle.app;

import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.conf.resolver.ServerInstanceResolver;
import pl.bristleback.server.bristle.conf.resolver.init.PojoConfigResolver;
import pl.bristleback.server.bristle.conf.resolver.message.ObjectSenderInjector;
import pl.bristleback.server.bristle.conf.resolver.message.RegisteredObjectSenders;
import pl.bristleback.server.bristle.conf.resolver.plain.PlainJavaApplicationComponentsContainer;
import pl.bristleback.server.bristle.conf.resolver.plain.PlainObjectSenderInjector;

public final class BristlebackBootstrap {

  private InitialConfigurationResolver initialConfigurationResolver;
  private PlainJavaApplicationComponentsContainer componentsContainer;
  private ObjectSenderInjector objectSenderInjector;

  private BristlebackBootstrap(InitialConfigurationResolver initialConfigurationResolver) {
    this.componentsContainer = new PlainJavaApplicationComponentsContainer();
    this.initialConfigurationResolver = initialConfigurationResolver;

    RegisteredObjectSenders registeredObjectSenders = new RegisteredObjectSenders();
    componentsContainer.addComponent(RegisteredObjectSenders.COMPONENT_NAME, registeredObjectSenders);

    objectSenderInjector = new PlainObjectSenderInjector(registeredObjectSenders);
  }

  public static BristlebackBootstrap init() {
    return new BristlebackBootstrap(new PojoConfigResolver());
  }

  public static BristlebackBootstrap init(InitialConfigurationResolver initialConfigurationResolver) {
    return new BristlebackBootstrap(initialConfigurationResolver);
  }

  public BristlebackServerInstance createServerInstance() {
    ServerInstanceResolver serverInstanceResolver = new ServerInstanceResolver(initialConfigurationResolver, componentsContainer);
    return serverInstanceResolver.resolverServerInstance();
  }

  public void registerActionClass(Object actionClass) {
    if (actionClass.getClass().getAnnotation(ActionClass.class) == null) {
      throw new IllegalArgumentException("Action class be annotated with " + ActionClass.class + ".");
    }
    registerComponent(actionClass);
  }

  private void registerComponent(Object component) {
    objectSenderInjector.injectSenders(component);
    componentsContainer.addComponent(component.getClass().getName(), component);
  }
}
