package pl.bristleback.client.actions.arguments;

import pl.bristleback.client.api.onmessage.MessageHandler;

/**
 * <p/>
 * Created on: 11.08.13 19:51 <br/>
 *
 * @author Pawel Machowski
 */
public abstract class BasicArgumentsHandler implements MessageHandler<String[]> {


  @Override
  public void onMessage(String[] payload) {
    Arguments arguments = new Arguments(payload);
    processArguments(arguments);
  }

  public abstract void processArguments(Arguments arguments);
}
