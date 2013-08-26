package pl.bristleback.client.connection;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p/>
 * Created on: 5/31/13 1:24 PM <br/>
 *
 * @author Pawel Machowski
 */
public class ServerUrl {

  private static final int DEFAULT_PORT = 8080;

  private String host = "localhost";
  private String fullUrl;

  private URI uri;

  public void setFullUrl(String fullUrl) {
    this.fullUrl = fullUrl;
  }

  public URI resolveUrl() {
    if (uri == null) {
      try {
        if (fullUrl != null) {
          uri = new URI(fullUrl);
        } else {
          uri = new URI("ws://" + host + ":" + DEFAULT_PORT + "/websocket");
        }
      } catch (URISyntaxException e) {
        throw new IllegalArgumentException("Malformed server URL. " + e);
      }
    }
    return uri;
  }
}
