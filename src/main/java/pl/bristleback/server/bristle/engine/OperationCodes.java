package pl.bristleback.server.bristle.engine;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-21 18:42:42 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum OperationCodes {

  TEXT_FRAME_CODE(0x01),
  BINARY_FRAME_CODE(0x02),
  CLOSE_FRAME_CODE(0x08);

  private int code;

  OperationCodes(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
