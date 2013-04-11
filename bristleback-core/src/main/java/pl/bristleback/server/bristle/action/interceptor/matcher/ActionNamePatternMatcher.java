package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;

/**
 * Checks if action should be intercepted using action class name.
 * <p/>
 * Note that this class uses <Strong>Bristleback action name - not {@link java.lang.reflect.Method} representation name</Strong>.
 * <p/>
 * Created on: 14.02.13 18:31 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionNamePatternMatcher extends AbstractPatternMatcher {

  @Override
  protected String getTestedString(ActionInformation actionInformation) {
    return actionInformation.getName();
  }
}
