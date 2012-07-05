package pl.bristleback.server.bristle.api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-04-25 17:05:35 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ServletServerEngine extends ServerEngine {

  void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
