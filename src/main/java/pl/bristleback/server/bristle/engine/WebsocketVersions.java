package pl.bristleback.server.bristle.engine;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-25 15:30:12 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum WebsocketVersions {

  HIXIE_76("0"),
  HYBI_10("8"),
  HYBI_13("13");

  private String versionCode;

  WebsocketVersions(String versionCode) {
    this.versionCode = versionCode;
  }

  public String getVersionCode() {
    return versionCode;
  }
}