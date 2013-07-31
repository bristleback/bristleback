package pl.bristleback.client.serialization;

import org.junit.Test;
import pl.bristleback.common.serialization.message.BristleMessage;

import static org.fest.assertions.Assertions.assertThat;

/**
 * <p/>
 * Created on: 5/31/13 1:12 PM <br/>
 *
 * @author Pawel Machowski
 */
public class ToJsonSerializerTest {

  private ToJsonSerializer toJsonSerializer = new ToJsonSerializer();

  @Test
  public void shouldSerializePerson() throws Exception {
    Person person = new Person();

    String serializedString = toJsonSerializer.objectToJson(person);

    assertThat(serializedString).isEqualTo("{\"name\":\"john\",\"age\":12}");
  }

  @Test
  public void shouldSerializeBristleMessageWithPerson() throws Exception {
    BristleMessage bristleMessage = new BristleMessage();
    bristleMessage.withId("123");
    bristleMessage.withName("name");
    bristleMessage.withPayload(new Person());


    String serializedString = toJsonSerializer.objectToJson(bristleMessage);

    assertThat(serializedString).isEqualTo("{\"id\":\"123\",\"name\":\"name\",\"payload\":{\"name\":\"john\",\"age\":12}}");
  }

}
