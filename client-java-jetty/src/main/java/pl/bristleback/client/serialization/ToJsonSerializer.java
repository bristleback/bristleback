package pl.bristleback.client.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * <p/>
 * Created on: 5/31/13 1:11 PM <br/>
 *
 * @author Pawel Machowski
 */
public class ToJsonSerializer {

  public String objectToJson(Object object) {
    ObjectMapper mapper = new ObjectMapper();

    try {
      return mapper.writeValueAsString(object);
    } catch (IOException e) {
      //TODO handle
      System.out.println(e);
    }
    return "";
  }
}
