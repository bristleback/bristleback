package pl.bristleback.client.serialization;

import org.junit.Test;

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


  private static class Person {
    private String name = "john";
    private int age = 12;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }
}
