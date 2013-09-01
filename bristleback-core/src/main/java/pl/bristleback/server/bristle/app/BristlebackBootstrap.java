package pl.bristleback.server.bristle.app;

import org.springframework.aop.framework.ProxyFactory;
import pl.bristleback.server.bristle.action.client.ClientActionProxyInterceptor;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.InitialConfigurationResolver;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.users.UserContextFactory;
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

    componentsContainer.addComponent(ClientActionProxyInterceptor.COMPONENT_NAME, new ClientActionProxyInterceptor());
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

  public BristlebackBootstrap registerActionClass(Object actionClass) {
    if (actionClass.getClass().getAnnotation(ActionClass.class) == null) {
      throw new IllegalArgumentException("Action class be annotated with " + ActionClass.class + ".");
    }
    registerComponent(actionClass);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T> T registerClientActionClass(T clientActionClass) {
    ProxyFactory factory = new ProxyFactory(clientActionClass);
    factory.addAdvice(componentsContainer.getBean(ClientActionProxyInterceptor.class));
    T component = (T) factory.getProxy();
    registerComponent(component);

    return component;
  }

  public BristlebackBootstrap registerConnectionStateListener(ConnectionStateListener listener) {
    registerComponent(listener);
    return this;
  }

  public BristlebackBootstrap registerUserContextFactory(UserContextFactory userContextFactory) {
    registerComponent(userContextFactory);
    return this;
  }

  public BristlebackBootstrap registerExceptionHandler(ActionExceptionHandler exceptionHandler) {
    registerComponent(exceptionHandler);
    return this;
  }

  public void registerComponent(Object component) {
    objectSenderInjector.injectSenders(component);
    componentsContainer.addComponent(component.getClass().getName(), component);
  }
}
