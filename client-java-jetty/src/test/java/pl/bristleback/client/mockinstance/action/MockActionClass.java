package pl.bristleback.client.mockinstance.action;

import org.springframework.stereotype.Controller;
import pl.bristleback.client.serialization.Person;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;

/**
 * <p/>
 * Created on: 31.07.13 21:10 <br/>
 *
 * @author Pawel Machowski
 */
@Controller
@ActionClass(name = "TestAction")
public class MockActionClass {

  @Action
  public Person testMethod(Person person) {
    return person;
  }

  @Action
  public Person incrementAge(Person person) {
    person.incrementAge();
    return person;
  }

}