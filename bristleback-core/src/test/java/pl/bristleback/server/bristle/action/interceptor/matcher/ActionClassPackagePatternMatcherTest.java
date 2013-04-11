package pl.bristleback.server.bristle.action.interceptor.matcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.mock.action.MockActionInterceptor;
import pl.bristleback.server.mock.action.SimpleActionClass;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class ActionClassPackagePatternMatcherTest extends AbstractPatternMatcherTest<ActionClassPackagePatternMatcher> {


  @Test
  public void testAllPatternsMatching() {
    //given
    setUpMatcher(new ActionClassPackagePatternMatcher(), "[a-zA-z\\.]*", ".+");
    ActionClassInformation actionClass = actionsContainer.getActionClass(SimpleActionClass.NAME);
    ActionInformation actionInformation = actionClass.getActions().get("unusualActionName");
    //when
    boolean result = matcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertTrue(result);
  }

  @Test
  public void testOnePatternMatching() {
    //given
    setUpMatcher(new ActionClassPackagePatternMatcher(), "xxx", ".+");
    ActionClassInformation actionClass = getActionsContainer().getActionClass(SimpleActionClass.NAME);
    ActionInformation actionInformation = actionClass.getActions().get("unusualActionName");
    //when
    boolean result = matcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertTrue(result);
  }

  @Test
  public void testNoPatternMatching() {
    //given
    setUpMatcher(new ActionClassPackagePatternMatcher(), "xxx", "yyy");
    ActionClassInformation actionClass = actionsContainer.getActionClass(SimpleActionClass.NAME);
    ActionInformation actionInformation = actionClass.getActions().get("unusualActionName");
    //when
    boolean result = matcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertFalse(result);
  }
}
