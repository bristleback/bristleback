package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;

/**
 * Checks if action should be intercepted using action class name.
 * <p/>
 * Note that this class uses <Strong>Bristleback action class name - not {@link Class} representation name</Strong>.
 * <p/>
 * Created on: 13.02.13 19:59 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionClassNamePatternMatcher extends AbstractPatternMatcher {

  @Override
  protected String getTestedString(ActionInformation actionInformation) {
    return actionInformation.getActionClass().getName();
  }
}
