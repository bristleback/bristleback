package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.client.ClientActionClassInformation;
import pl.bristleback.server.mock.action.ExtendingClientActionClass;
import pl.bristleback.server.mock.action.client.MockClientActionClass;
import pl.bristleback.server.mock.action.NonStandardClientActionClass;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;

import javax.inject.Inject;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-21 14:26:14 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class ClientActionClassesResolverTest {

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private ClientActionClassesResolver clientActionClassesResolver;

  @Before
  public void setUp() {
    clientActionClassesResolver = mockBeansFactory.getFrameworkBean("clientActionClassesResolver", ClientActionClassesResolver.class);
  }

  @Test
  public void shouldProperlyResolveActionClassNameFromClass() {
    //given
    String actionClassNameFromClassName = MockClientActionClass.class.getSimpleName();

    //when
    Map<String, ClientActionClassInformation> actionClasses = clientActionClassesResolver.resolve();

    //then
    assertTrue(actionClasses.containsKey(actionClassNameFromClassName));
    ClientActionClassInformation namedFromClassName = actionClasses.get(actionClassNameFromClassName);
    assertNotNull(namedFromClassName);
    assertEquals(actionClassNameFromClassName, namedFromClassName.getName());
  }

  @Test
  public void shouldProperlyResolveActionClassNameNonStandard() {
    //given

    //when
    Map<String, ClientActionClassInformation> actionClasses = clientActionClassesResolver.resolve();

    //then
    assertTrue(actionClasses.containsKey(NonStandardClientActionClass.NAME));
    ClientActionClassInformation namedFromClassName = actionClasses.get(NonStandardClientActionClass.NAME);
    assertNotNull(namedFromClassName);
    assertEquals(NonStandardClientActionClass.NAME, namedFromClassName.getName());
  }

  @Test
  public void shouldProperlyResolveActionClassExtending() {
    //given
    String actionClassNameFromClassName = ExtendingClientActionClass.class.getSimpleName();

    //when
    Map<String, ClientActionClassInformation> actionClasses = clientActionClassesResolver.resolve();

    //then
    assertTrue(actionClasses.containsKey(actionClassNameFromClassName));
    ClientActionClassInformation namedFromClassName = actionClasses.get(actionClassNameFromClassName);
    assertNotNull(namedFromClassName);
    assertEquals(actionClassNameFromClassName, namedFromClassName.getName());
    assertEquals(3, namedFromClassName.getClientActions().size());
  }
}
