package pl.bristleback.server.bristle.engine;

/**
 * Mapping between Websocket Draft/Specification version and value of <code>Sec-WebSocket-Version</code> header
 * that should be sent by the client on opening handshake to be successfully handled by the server.
 * <p/>
 * Created on: 2011-09-25 15:30:12 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum WebsocketVersions {

  HIXIE_76("0"),
  HYBI_10("8"),
  HYBI_13("13"),
  RFC_6455("13");

  private String versionCode;

  WebsocketVersions(String versionCode) {
    this.versionCode = versionCode;
  }

  public String getVersionCode() {
    return versionCode;
  }
}