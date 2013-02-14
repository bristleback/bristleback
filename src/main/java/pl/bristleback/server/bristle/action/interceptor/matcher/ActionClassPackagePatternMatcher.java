package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;

/**
 * Checks if action should be intercepted using action class package name.
 * <p/>
 * Created on: 12.02.13 19:34 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionClassPackagePatternMatcher extends AbstractPatternMatcher {

  @Override
  protected String getTestedString(ActionInformation actionInformation) {
    return actionInformation.getActionClass().getType().getPackage().getName();
  }
}
