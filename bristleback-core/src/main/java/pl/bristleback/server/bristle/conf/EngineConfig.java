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

package pl.bristleback.server.bristle.conf;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Configuration of the Websocket engine, used by {@link pl.bristleback.server.bristle.api.ServerEngine} implementations.
 * Note that each field usage may be dependent on websocket engine implementations.
 * For example, <code>port</code> configuration property is not used in web server implementations
 * (engines implementing {@link pl.bristleback.server.bristle.api.ServletServerEngine} interface).
 * <p/>
 * Created on: 2011-07-03 23:36:15 <br/>
 *
 * @author Wojciech Niemiec
 */
public class EngineConfig {

  private String name;

  private int port;

  private int maxFrameSize;

  private int maxBufferSize;

  private int timeout;

  private List<String> rejectedDomains;

  private Map<String, String> properties;

  private AtomicLong currentId = new AtomicLong(1);

  public long getNextConnectorId() {
    return currentId.getAndIncrement();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public int getMaxFrameSize() {
    return maxFrameSize;
  }

  public void setMaxFrameSize(int maxFrameSize) {
    this.maxFrameSize = maxFrameSize;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public List<String> getRejectedDomains() {
    return rejectedDomains;
  }

  public void setRejectedDomains(List<String> rejectedDomains) {
    this.rejectedDomains = rejectedDomains;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public int getMaxBufferSize() {
    return maxBufferSize;
  }

  public void setMaxBufferSize(int maxBufferSize) {
    this.maxBufferSize = maxBufferSize;
  }
}
