package pl.bristleback.client;

import pl.bristleback.client.actions.arguments.Arguments;
import pl.bristleback.client.actions.arguments.BasicArgumentsHandler;
import pl.bristleback.client.serialization.Person;

/**
 * <p/>
 * Created on: 01.09.13 21:18 <br/>
 *
 * @author Pawel Machowski
 */
public class TestMethodHandler extends BasicArgumentsHandler{

    private Person result;

    @Override
    public void processArguments(Arguments arguments) {
      this.result = arguments.get(0, Person.class);
    }

    public Person getResult() {
      return result;
    }


}
