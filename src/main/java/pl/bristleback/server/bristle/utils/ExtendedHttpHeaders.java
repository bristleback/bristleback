package pl.bristleback.server.bristle.utils;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-14 11:16:53 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class ExtendedHttpHeaders {

  private ExtendedHttpHeaders() {
    throw new UnsupportedOperationException();
  }

  public static final class Names {

    private Names() {
      throw new UnsupportedOperationException();
    }

    public static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
    public static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
    public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
  }


}