package pl.bristleback.client.serialization;

/**
 * <p/>
 * Created on: 31.07.13 20:33 <br/>
 *
 * @author Pawel Machowski
 */
public class Person {

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

  public void incrementAge() {
    age++;
  }
}
