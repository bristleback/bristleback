package pl.bristleback.server.bristle.serialization.jackson;

import com.fasterxml.jackson.databind.JavaType;
import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-03-09 18:46:23 <br/>
 *
 * @author Wojciech Niemiec
 */
public class JacksonSerialization {
  private static Logger log = Logger.getLogger(JacksonSerialization.class.getName());

  //  private TypeReference typeReference;
  private JavaType type;

  public JavaType getType() {
    return type;
  }

  public void setType(JavaType type) {
    this.type = type;
  }
}
