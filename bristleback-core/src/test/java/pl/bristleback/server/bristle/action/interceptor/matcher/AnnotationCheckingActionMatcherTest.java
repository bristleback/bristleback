package pl.bristleback.server.bristle.action.interceptor.matcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.annotations.Authorized;
import pl.bristleback.server.mock.action.MockActionInterceptor;
import pl.bristleback.server.mock.action.SimpleActionClass;

import java.util.Arrays;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class AnnotationCheckingActionMatcherTest extends AbstractMatcherTest {

  @Test
  @SuppressWarnings("unchecked")
  public void shouldInterceptorBeApplicable() throws Exception {
    //given
    AnnotationCheckingActionMatcher matcher = new AnnotationCheckingActionMatcher(Authorized.class);

    ActionClassInformation actionClass = actionsContainer.getActionClass(SimpleActionClass.NAME);
    ActionInformation actionInformation = actionClass.getActions().get("unusualActionName");
    //when
    boolean result = matcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertTrue(result);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldInterceptorBeNotApplicable() throws Exception {
    //given
    AnnotationCheckingActionMatcher matcher = new AnnotationCheckingActionMatcher();
    matcher.setAnnotationClasses(Arrays.asList(Authorized.class, Test.class));

    ActionClassInformation actionClass = actionsContainer.getActionClass(SimpleActionClass.NAME);
    ActionInformation actionInformation = actionClass.getActions().get("unusualActionName");
    //when
    boolean result = matcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertFalse(result);
  }
}
