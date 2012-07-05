package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.response.ActionResponseInformation;
import pl.bristleback.server.bristle.api.action.ActionInformation;
import pl.bristleback.server.bristle.engine.base.users.DefaultUser;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.action.SimpleActionClass;

import javax.inject.Inject;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-08 21:13:50 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class ActionResolverTest {
  private static Logger log = Logger.getLogger(ActionResolverTest.class.getName());

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private ActionResolver actionResolver;

  @Before
  public void setUp() {
    actionResolver = mockBeansFactory.getFrameworkBean("actionResolver", ActionResolver.class);
  }

  @Test
  public void testDefaultAction() throws NoSuchMethodException {
    //given
    Class<SimpleActionClass> simpleActionClass = SimpleActionClass.class;
    Method defaultAction = simpleActionClass.getMethod("executeDefault", DefaultUser.class, String.class);

    //when
    ActionInformation actionInformation = actionResolver.prepareActionInformation(simpleActionClass, defaultAction);

    //then
    assertTrue(actionInformation.isDefaultAction());
    assertEquals("executeDefault", actionInformation.getName());
    assertNotNull(actionInformation.getResponseInformation());
  }

  @Test
  public void testWithNonDefaultResponse() throws NoSuchMethodException {
    //given
    Class<SimpleActionClass> simpleActionClass = SimpleActionClass.class;
    Method defaultAction = simpleActionClass.getMethod("nonDefaultAction", String.class);

    //when
    ActionInformation actionInformation = actionResolver.prepareActionInformation(simpleActionClass, defaultAction);

    //then
    assertFalse(actionInformation.isDefaultAction());
    assertEquals("unusualActionName", actionInformation.getName());
    ActionResponseInformation responseInformation = actionInformation.getResponseInformation();
    assertNotNull(responseInformation);
    Object serialization = responseInformation.getSerialization();
    assertNotNull(serialization);
  }
}
