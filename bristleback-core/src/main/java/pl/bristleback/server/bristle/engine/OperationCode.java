package pl.bristleback.server.bristle.engine;

/**
 * Websocket op-codes defined in <a href="http://tools.ietf.org/html/rfc6455">Websocket RFC</a>.
 * <p/>
 * Created on: 2011-11-21 18:42:42 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum OperationCode {

  TEXT_FRAME_CODE(0x01),
  BINARY_FRAME_CODE(0x02),
  CLOSE_FRAME_CODE(0x08),
  PING_FRAME_CODE(0x09),
  PONG_FRAME_CODE(0x0A);

  private int code;

  OperationCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
