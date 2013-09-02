package pl.bristleback.test;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

  private String firstName;

  private String lastName;

  private int age;

  @JsonIgnore
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
