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

package pl.bristleback.server.bristle.engine.netty;

import org.jboss.netty.channel.Channel;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.ServerEngine;
import pl.bristleback.server.bristle.engine.base.AbstractConnector;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-15 16:42:57 <br/>
 *
 * @author Wojciech Niemiec
 */
public class NettyConnector extends AbstractConnector {

  private Channel channel;

  public NettyConnector(Channel channel, ServerEngine engine, DataController controller) {
    super(engine, controller);
    this.channel = channel;
  }

  @Override
  public void stop() {
    channel.disconnect();
  }

  public Channel getChannel() {
    return channel;
  }
}