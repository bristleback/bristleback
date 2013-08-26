package pl.bristleback.client.serialization;

import org.junit.Test;
import pl.bristleback.common.serialization.message.BristleMessage;

import static org.fest.assertions.Assertions.assertThat;

/**
 * <p/>
 * Created on: 31.07.13 20:32 <br/>
 *
 * @author Pawel Machowski
 */
public class FromJsonDeserializerTest {

  private FromJsonDeserializer fromJsonDeserializer = new FromJsonDeserializer();

  @Test
  public void shouldSerializeBristleMessageWithPerson() throws Exception {
    BristleMessage bristleMessage = new BristleMessage();
    bristleMessage.withId("123");
    bristleMessage.withName("name");
    bristleMessage.withPayload(new Person());


    BristleMessage<String[]> deserialized = fromJsonDeserializer.jsonToBristleMessage("{\"id\":\"123\",\"name\":\"name\",\"payload\":[{\"name\":\"john\",\"age\":12}]}");

    assertThat(deserialized.getName()).isEqualTo("name");
  }
}
