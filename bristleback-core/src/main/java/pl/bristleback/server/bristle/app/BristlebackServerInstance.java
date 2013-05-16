/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

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

    System.out.println("\n\n"
      + "--------------------------------------------------------\n"
      + "--- BRISTLEBACK FRAMEWORK, version " + BRISTLEBACK_VERSION + " ---\n"
      + "--- " + BRISTLEBACK_HOMEPAGE + "                            ---\n"
      + "--- BRISTLEBACK FRAMEWORK STARTED                    ---\n"
      + "--------------------------------------------------------\n\n");
  }

  public void stopServer() {
    running = false;
    messageDispatcher.stopDispatching();
    engine.stopServer();

    System.out.println("\n\n"
      + "--------------------------------------------------------\n"
      + "--- BRISTLEBACK FRAMEWORK, version " + BRISTLEBACK_VERSION + " ---\n"
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

