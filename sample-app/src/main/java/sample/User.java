package sample;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-02 15:22:34 <br/>
 *
 * @author Wojciech Niemiec
 */
public class User {
  private static Logger log = Logger.getLogger(User.class.getName());

  private String firstName;
  private String lastName;
  private int age;

  private User friend;

  public User getFriend() {
    return friend;
  }

  public void setFriend(User friend) {
    this.friend = friend;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}