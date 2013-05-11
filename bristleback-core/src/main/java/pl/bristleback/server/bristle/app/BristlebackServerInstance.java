package pl.bristleback.server.bristle.app;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.MessageDispatcher;
import pl.bristleback.server.bristle.api.ServerEngine;

/**
 * Representation of single Bristleback Websocket server instance.
 * It is used by both {@link StandaloneServerRunner} and
 * {@link pl.bristleback.server.bristle.engine.servlet.BristlebackHttpHandler}.
 * <p/>
 * Created on: 2012-01-22 22:00:29 <br/>
 *
 * @author Wojciech Niemiec
 */
public class BristlebackServerInstance {

  public static final String BRISTLEBACK_VERSION = "0.3.0";

  public static final String BRISTLEBACK_HOMEPAGE = "http://bristleback.pl";

  private static Logger log = Logger.getLogger(BristlebackServerInstance.class.getName());

  private boolean running;

  private ServerEngine engine;

  private MessageDispatcher messageDispatcher;

  private BristlebackConfig configuration;

  public BristlebackServerInstance(BristlebackConfig configuration) {
    this.configuration = configuration;
    engine = configuration.getServerEngine();
    messageDispatcher = configuration.getMessageConfiguration().getMessageDispatcher();
  }

  public void startServer() {
    running = true;
    engine.startServer();
    messageDispatcher.startDispatching();

    log.debug("\n\n"
      + "--------------------------------------------------------\n"
      + "--- BRISTLEBACK FRAMEWORK, version " + BRISTLEBACK_VERSION + "             ---\n"
      + "--- " + BRISTLEBACK_HOMEPAGE + "                            ---\n"
      + "--- BRISTLEBACK FRAMEWORK STARTED                    ---\n"
      + "--------------------------------------------------------\n\n");
  }

  public void stopServer() {
    running = false;
    messageDispatcher.stopDispatching();
    engine.stopServer();

    log.debug("\n\n"
      + "--------------------------------------------------------\n"
      + "--- BRISTLEBACK FRAMEWORK, version " + BRISTLEBACK_VERSION + "             ---\n"
      + "--- " + BRISTLEBACK_HOMEPAGE + "                            ---\n"
      + "--- BRISTLEBACK FRAMEWORK STOPPED                    ---\n"
      + "--------------------------------------------------------\n\n");
  }

  public boolean isRunning() {
    return running;
  }

  public BristlebackConfig getConfiguration() {
    return configuration;
  }
}

