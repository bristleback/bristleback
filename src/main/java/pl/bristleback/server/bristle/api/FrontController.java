package pl.bristleback.server.bristle.api;

/**
 * Implementations of this interface should be able to handle every control and non control message,
 * coming from low level Websocket connector implementation.
 * <p/>
 * Created on: 2011-11-22 17:52:26 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.engine.controller.DefaultFrontController DefaultFrontController
 */
public interface FrontController {

  /**
   * Processes incoming command, with unspecified type of data and operation code given as integer number.
   *
   * @param connector     low level, server engine dependent abstraction of client-server connection.
   * @param operationCode code of requested operation. List of available operation code is specified by WebSockets protocol
   *                      and also can be found in {@link pl.bristleback.server.bristle.engine.OperationCodes OperationCodes} enumeration.
   * @param data          data sent by client.
   * @see pl.bristleback.server.bristle.engine.OperationCodes Operation codes
   */
  void processCommand(WebsocketConnector connector, int operationCode, Object data);
}
