package pl.bristleback.server.bristle.conf;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * //@todo class description
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
