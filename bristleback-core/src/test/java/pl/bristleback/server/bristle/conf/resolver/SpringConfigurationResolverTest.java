package pl.bristleback.server.bristle.conf.resolver;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import pl.bristleback.server.bristle.api.ConnectionStateListener;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.conf.BristlebackComponentsContainer;
import pl.bristleback.server.bristle.conf.resolver.spring.SpringApplicationComponentsResolver;
import pl.bristleback.server.bristle.conf.resolver.spring.SpringConfigurationResolver;
import pl.bristleback.server.bristle.listener.ConnectionStateListenerChain;
import pl.bristleback.server.bristle.listener.ListenersContainer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpringConfigurationResolverTest {

  private ConnectionStateListener withoutAnnotation = new ConnectionStateListenerWithoutOrder();

  private ConnectionStateListener withoutOrder = new ConnectionStateListenerWithoutOrder();

  private ConnectionStateListener order1 = new ConnectionStateListenerOrder1();

  private ConnectionStateListener order2 = new ConnectionStateListenerOrder2();

  private ApplicationContext bristlebackFrameworkContextMock;

  private SpringConfigurationResolver resolver;

  @Before
  public void setUp() {
    ApplicationContext applicationContextMock = mock(ApplicationContext.class);
    bristlebackFrameworkContextMock = mock(ApplicationContext.class);
    SpringApplicationComponentsResolver componentsResolver = new SpringApplicationComponentsResolver(applicationContextMock);
    BristlebackComponentsContainer springIntegrationMock = new BristlebackComponentsContainer(componentsResolver, bristlebackFrameworkContextMock);
    resolver = new SpringConfigurationResolver();
    resolver.setComponentsContainer(springIntegrationMock);
  }

  @Test
  public void shouldShouldSortInIncreasingOrder() {
    // given
    Map<String, ConnectionStateListener> connectionListeners = new LinkedHashMap<String, ConnectionStateListener>();
    connectionListeners.put("one", withoutOrder);
    connectionListeners.put("two", order1);
    connectionListeners.put("three", order2);
    connectionListeners.put("four", withoutAnnotation);
    when(bristlebackFrameworkContextMock.getBeansOfType(ConnectionStateListener.class)).thenReturn(connectionListeners);

    // when
    ListenersContainer listenersContainer = resolver.listenersContainer();

    // then
    assertThat(listenersContainer).isNotNull();

    List<ConnectionStateListener> connectionStateListeners = listenersContainer.getConnectionStateListeners();
    assertThat(connectionStateListeners)
      .isNotNull()
      .hasSize(connectionListeners.size())
      .containsSequence(order1, order2);
  }

  @Test
  public void shouldShouldSortInIncreasingOrder2() {
    // given
    Map<String, ConnectionStateListener> connectionListeners = new LinkedHashMap<String, ConnectionStateListener>();
    connectionListeners.put("one", order1);
    connectionListeners.put("two", withoutOrder);
    connectionListeners.put("three", order2);
    connectionListeners.put("four", withoutAnnotation);
    when(bristlebackFrameworkContextMock.getBeansOfType(ConnectionStateListener.class)).thenReturn(connectionListeners);

    // when
    ListenersContainer listenersContainer = resolver.listenersContainer();

    // then
    assertThat(listenersContainer).isNotNull();

    List<ConnectionStateListener> connectionStateListeners = listenersContainer.getConnectionStateListeners();
    assertThat(connectionStateListeners)
      .isNotNull()
      .hasSize(connectionListeners.size())
      .containsSequence(order1, order2);
  }

  @Test
  public void shouldShouldSortInIncreasingOrder3() {
    // given
    Map<String, ConnectionStateListener> connectionListeners = new LinkedHashMap<String, ConnectionStateListener>();
    connectionListeners.put("one", order1);
    connectionListeners.put("two", withoutOrder);
    connectionListeners.put("three", withoutAnnotation);
    connectionListeners.put("four", order2);
    when(bristlebackFrameworkContextMock.getBeansOfType(ConnectionStateListener.class)).thenReturn(connectionListeners);

    // when
    ListenersContainer listenersContainer = resolver.listenersContainer();

    // then
    assertThat(listenersContainer).isNotNull();

    List<ConnectionStateListener> connectionStateListeners = listenersContainer.getConnectionStateListeners();
    assertThat(connectionStateListeners)
      .isNotNull()
      .hasSize(connectionListeners.size())
      .containsSequence(order1, order2);
  }

  @Test
  public void shouldShouldSortInIncreasingOrder4() {
    // given
    Map<String, ConnectionStateListener> connectionListeners = new LinkedHashMap<String, ConnectionStateListener>();
    connectionListeners.put("one", order1);
    connectionListeners.put("two", order2);
    connectionListeners.put("three", withoutOrder);
    connectionListeners.put("four", withoutAnnotation);
    when(bristlebackFrameworkContextMock.getBeansOfType(ConnectionStateListener.class)).thenReturn(connectionListeners);

    // when
    ListenersContainer listenersContainer = resolver.listenersContainer();

    // then
    assertThat(listenersContainer).isNotNull();

    List<ConnectionStateListener> connectionStateListeners = listenersContainer.getConnectionStateListeners();
    assertThat(connectionStateListeners)
      .isNotNull()
      .hasSize(connectionListeners.size())
      .containsSequence(order1, order2);
  }

  @Order(1)
  private class ConnectionStateListenerOrder1 implements ConnectionStateListener {

    @Override
    public void userConnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    }

    @Override
    public void userDisconnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    }
  }

  @Order(2)
  private class ConnectionStateListenerOrder2 implements ConnectionStateListener {

    @Override
    public void userConnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    }

    @Override
    public void userDisconnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    }
  }

  private class ConnectionStateListenerWithoutOrder implements ConnectionStateListener {

    @Override
    public void userConnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    }

    @Override
    public void userDisconnected(UserContext userContext, ConnectionStateListenerChain connectionStateListenerChain) {
    }
  }
}
