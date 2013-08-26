package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.utils.StringUtils;
import pl.bristleback.server.mock.action.client.MockClientActionClass;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import javax.inject.Inject;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-21 15:09:39 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class ClientActionResolverTest {

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private ClientActionResolver clientActionResolver;


  @Before
  public void setUp() {
    clientActionResolver = mockBeansFactory.getFrameworkBean("clientActionResolver", ClientActionResolver.class);
  }

  @Test
  public void shouldResolveActionInformationProperly() throws NoSuchMethodException {
    //given
    Method method = MockClientActionClass.class.getMethod(MockClientActionClass.SIMPLE_ACTION_NAME,
      String.class, VerySimpleMockBean.class, UserContext.class);

    String actionClassName = MockClientActionClass.class.getSimpleName();

    //when
    ClientActionInformation actionInformation = clientActionResolver.prepareActionInformation(actionClassName, method);

    //then
    assertEquals(MockClientActionClass.SIMPLE_ACTION_NAME, actionInformation.getName());
    String expectedFullName = actionClassName + StringUtils.DOT_AS_STRING + MockClientActionClass.SIMPLE_ACTION_NAME;
    assertEquals(expectedFullName, actionInformation.getFullName());
    assertEquals(3, actionInformation.getParameters().size());
    assertNotNull(actionInformation.getResponse());
    assertNotNull(actionInformation.getParameters().get(0).getSerialization());
  }
}
