package pl.bristleback.server.bristle.api;

/**
 * Message dispatcher is responsible for collecting all outbound messages and sending them in thread safe way.
 * The idea of dispatcher is to guarantee that no matter from how many threads will send message by
 * {@link pl.bristleback.server.bristle.message.ConditionObjectSender} objects,
 * those messages will be redirected to dispatcher which then sends them to server engine.
 * Implementations must be able to send single targeted message or broadcasting messages.
 * Dispatchers should not perform filtering or any extra operations related with choosing sending target.
 * Only one instance of dispatcher is created at server start.
 * Any new threads used by dispatcher should be interrupted in {@link MessageDispatcher#stopDispatching()} method.
 * In future versions, messages will contain priority, so dispatcher will be able to sort messages into more and less important.
 * In addition, future versions will be able to make use of configuration file, for example, to specify interval of message dispatching.
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
