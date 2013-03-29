package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.ActionClassInformation;
import pl.bristleback.server.bristle.action.ActionsContainer;
import pl.bristleback.server.bristle.action.exception.BrokenActionProtocolException;
import pl.bristleback.server.mock.action.SimpleActionClass;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-04 09:54:19 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class ActionClassesResolverTest {
  private static Logger log = Logger.getLogger(ActionClassesResolverTest.class.getName());

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private ActionClassesResolver actionClassesResolver;

  @Before
  public void setUp() {
    actionClassesResolver = mockBeansFactory.getFrameworkBean("actionClassesResolver", ActionClassesResolver.class);
  }


  @Test
  public void shouldResolveCorrectly() {
    //given

    //when
    ActionsContainer container = actionClassesResolver.resolve();

    //then
    assertNotNull(container);
    ActionClassInformation classInformation = container.getActionClass(SimpleActionClass.NAME);
    assertNotNull(classInformation);
    assertEquals(SimpleActionClass.NAME, classInformation.getName());
    assertEquals("sampleActionBean", classInformation.getSpringBeanName());
    assertEquals(SimpleActionClass.class, classInformation.getType());
    assertTrue(classInformation.isSingleton());
    assertNotNull(classInformation.getSingletonActionClassInstance());
  }

  @Test(expected = BrokenActionProtocolException.class)
  public void shouldResolveCorrectlyNotAnnotatedNotAdded() {
    //given

    //when
    ActionsContainer container = actionClassesResolver.resolve();

    //then
    assertNotNull(container);
    container.getActionClass("notAnnotatedAction");
  }
}
