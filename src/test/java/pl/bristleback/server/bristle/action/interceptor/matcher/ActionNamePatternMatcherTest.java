package pl.bristleback.server.bristle.action.interceptor.matcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.mock.action.MockActionInterceptor;
import pl.bristleback.server.mock.action.SimpleActionClass;

import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class ActionNamePatternMatcherTest extends AbstractPatternMatcherTest<ActionNamePatternMatcher> {

  @Test
  public void testGetTestedString() throws Exception {
    //given
    setUpMatcher(new ActionNamePatternMatcher(), "xa", "unusualActionName");
    ActionClassInformation actionClass = actionsContainer.getActionClass(SimpleActionClass.NAME);
    ActionInformation actionInformation = actionClass.getActions().get("unusualActionName");
    //when
    boolean result = matcher.isInterceptorApplicable(actionInformation,MockActionInterceptor.class);

    //then
    assertTrue(result);
  }
}
