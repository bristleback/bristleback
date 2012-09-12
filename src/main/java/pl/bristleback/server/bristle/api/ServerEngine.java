package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.conf.EngineConfig;

/**
 * This interface is a most important part of connection module.
 * Main purpose of this module is to handle client connection, including opening and closing handshakes,
 * receiving incoming frames and forwarding them to {@link pl.bristleback.server.bristle.api.FrontController}
 * and of course sending frames to client. Server engine must properly implements WebSockets protocol by processing
 * the data, decoding and encoding frames. To handle single connections,
 * engine creates object of {@link pl.bristleback.server.bristle.api.WebsocketConnector} implementation.
 * Server engine use special part of configuration, {@link pl.bristleback.server.bristle.conf.EngineConfig Engine Configuration}.
 * Every implementation should inherit from {@link pl.bristleback.server.bristle.engine.base.AbstractServerEngine} class,
 * which has more than a half methods implemented.
 * <p/>
 * Bristleback Server has several built in server engine implementations:
 * <table>
 * <thead>
 * <tr>
 * <td>
 * <strong>
 * Server engine name
 * </strong>
 * </td>
 * <td>
 * <strong>
 * Server engine class
 * </strong>
 * </td>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>
 * system.engine.jetty
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.engine.jetty.JettyWebsocketEngine}
 * </td>
 * </tr>
 * <tr>
 * <td>
 * system.engine.netty
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.engine.netty.NettyServerEngine}
 * </td>
 * </tr>
 * <tr>
 * <td>
 * system.engine.jetty.servlet
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.engine.jetty.servlet.JettyServletWebsocketEngine}
 * </td>
 * </tr>
 * <tr>
 * <td>
 * system.engine.tomcat.servlet
 * </td>
 * <td>
 * {@link pl.bristleback.server.bristle.engine.tomcat.servlet.TomcatServletWebsocketEngine}
 * </td>
 * </tr>
 * </tbody>
 * </table>
 * <p/>
 * Created on: 2011-07-05 14:51:41 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ServerEngine extends ConfigurationAware {

  /**
   * Gets server engine configuration.
   *
   * @return engine configuration.
   */
  EngineConfig getEngineConfiguration();

  /**
   * Gets Bristleback Server configuration.
   *
   * @return configuration.
   */
  BristlebackConfig getConfiguration();

  /**
   * This method is invoked after all server components are in valid initialized state.
   * On that point, server starts to listening incoming connection.
   */
  void startServer();

  /**
   * Stops server, previously closing all open connections.
   */
  void stopServer();

  /**
   * When a new connection is established, a newly created connector invokes this callback method to signal server engine.
   * Engine should always run
   * {@link pl.bristleback.server.bristle.listener.ConnectionStateListenerChain#connectorStarted(pl.bristleback.server.bristle.api.users.IdentifiedUser) ConnectionStateListenerChain.connectorStarted()} method.
   *
   * @param connector newly created connector representing established client connection.
   */
  void onConnectionOpened(WebsocketConnector connector);

  /**
   * This callback method is called by a connector when connection with client is closed. Connection might be closed for several reasons:
   * <ul>
   * <li>
   * Connection is closed by client side
   * </li>
   * <li>
   * Connection is closed because maximum idle time is exceeded.
   * </li>
   * <li>
   * Connection is closed by sending closing server message using {@link pl.bristleback.server.bristle.message.ConditionObjectSender ConditionObjectSender}.
   * </li>
   * </ul>
   * Engine should always run
   * {@link pl.bristleback.server.bristle.listener.ConnectionStateListenerChain#connectorStopped(pl.bristleback.server.bristle.api.users.IdentifiedUser) ConnectionStateListenerChain.connectorStopped()} method.
   *
   * @param connector object representing connection with client.
   */
  void onConnectionClose(WebsocketConnector connector);

  /**
   * Sends a text message to a given recipient. If necessary, message should be split into multiple frames.
   * Frame format must be consistent with WebSockets protocol format. Text message has operation code with value 0x01.
   *
   * @param connector       recipient of the message.
   * @param contentAsString text message content.
   * @throws Exception if any exception while sending message occurs.
   * @see pl.bristleback.server.bristle.engine.OperationCodes OperationCodes
   */
  void sendMessage(WebsocketConnector connector, String contentAsString) throws Exception;

  /**
   * Sends a binary message to a given recipient. If necessary, message should be split into multiple frames.
   * Frame format must be consistent with WebSockets protocol format. Binary message has operation code with value 0x02.
   *
   * @param connector      recipient of the message.
   * @param contentAsBytes binary message content.
   * @throws Exception if any exception while sending message occurs.
   */
  void sendMessage(WebsocketConnector connector, byte[] contentAsBytes) throws Exception;
}