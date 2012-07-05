package pl.bristleback.server.bristle.api;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-05 20:33:29 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface MessageDispatcher {

  /**
   * Adds message containing token and one or more target connectors.
   * Adding messages should be thread safe, which means,
   * any thread invoking this method must be able to do so without any synchronisation from user side.
   *
   * @param message message.
   */
  void addMessage(WebsocketMessage message);

  /**
   * Dispatches collected messages and removing them from collection.
   *
   * @throws Exception if any exception occurs.
   */
  void dispatchMessages() throws Exception;

  /**
   * Describes operations, like dispatcher thread start,
   * which will be invoked when framework will be ready for sending messages.
   * This method is invoked by plugin and should not be used by user.
   */
  void startDispatching();

  /**
   * Operations executed when framework stops and no messages can be sent anymore.
   * If any messages remain in collection while this method is being invoked,
   * they can not be sent because of framework engine stop.
   */
  void stopDispatching();

  /**
   * Sets server implementing jwebsocket framework server interface.
   * This method is invoked by plugin internally at plugin start.
   *
   * @param server jwebsocket server implementation.
   */
  void setServer(ServerEngine server);

}
