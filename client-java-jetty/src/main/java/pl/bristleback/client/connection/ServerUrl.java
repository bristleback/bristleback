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

  private String host = "localhost";
  private static int port = 8080;
  private String fullUrl;

  private URI uri;

  public void setFullUrl(String fullUrl) {
    this.fullUrl = fullUrl;
  }

  public URI resolveUrl() {
    if(uri == null){
    try {
      if (fullUrl != null) {
        uri = new URI(fullUrl);
      } else {
        uri = new URI("ws://" + host + ":" + port + "/websocket");
      }
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Malformed server URL. " + e);
    }
    }
    return uri;
  }
}
