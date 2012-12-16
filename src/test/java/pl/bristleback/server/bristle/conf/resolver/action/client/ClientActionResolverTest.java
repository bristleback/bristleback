package pl.bristleback.server.bristle.conf.resolver.action.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.client.ClientActionInformation;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.PropertyType;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
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
      String.class, VerySimpleMockBean.class, IdentifiedUser.class);

    String actionClassName = MockClientActionClass.class.getSimpleName();

    //when
    ClientActionInformation actionInformation = clientActionResolver.prepareActionInformation(actionClassName, method);

    //then
    assertEquals(MockClientActionClass.SIMPLE_ACTION_NAME, actionInformation.getName());
    String expectedFullName = actionClassName + StringUtils.DOT_AS_STRING + MockClientActionClass.SIMPLE_ACTION_NAME;
    assertEquals(expectedFullName, actionInformation.getFullName());
    assertEquals(3, actionInformation.getParameters().size());
    assertNotNull(actionInformation.getResponse());
    assertNotNull(actionInformation.getSerialization());
  }

  @Test
  public void shouldResolveActionInformationProperlyMultipleParamsSerialized() throws NoSuchMethodException {
    //given
    Method method = MockClientActionClass.class.getMethod(MockClientActionClass.SIMPLE_ACTION_NAME,
      String.class, VerySimpleMockBean.class, IdentifiedUser.class);

    String actionClassName = MockClientActionClass.class.getSimpleName();

    //when
    ClientActionInformation actionInformation = clientActionResolver.prepareActionInformation(actionClassName, method);

    //then
    PropertySerialization serialization = (PropertySerialization) actionInformation.getSerialization();
    PropertySerialization payloadSerialization = serialization.getPropertySerialization(BristleMessage.PAYLOAD_PROPERTY_NAME);
    assertNotNull(payloadSerialization);
    assertEquals(PropertyType.MAP, payloadSerialization.getPropertyType());
    int expectedNumberOfParameterSerializations = 2;
    int expectedNumberOfSerializations = expectedNumberOfParameterSerializations + 1; // one for default element serialization
    assertEquals(expectedNumberOfSerializations, payloadSerialization.getPropertiesInformation().size());
  }

  @Test
  public void shouldResolveActionInformationProperlyMultipleParamsButOneSerialized() throws NoSuchMethodException {
    //given
    Method method = MockClientActionClass.class.getMethod(MockClientActionClass.MULTIPLE_PARAMS_ONE_SERIALIZED_ACTION_NAME,
      String.class, String.class, IdentifiedUser.class);

    String actionClassName = MockClientActionClass.class.getSimpleName();

    //when
    ClientActionInformation actionInformation = clientActionResolver.prepareActionInformation(actionClassName, method);

    //then
    PropertySerialization serialization = (PropertySerialization) actionInformation.getSerialization();
    PropertySerialization payloadSerialization = serialization.getPropertySerialization(BristleMessage.PAYLOAD_PROPERTY_NAME);
    assertNotNull(payloadSerialization);
    assertEquals(PropertyType.SIMPLE, payloadSerialization.getPropertyType());
  }

  @Test
  public void shouldResolveActionInformationProperlySingleParam() throws NoSuchMethodException {
    //given
    Method method = MockClientActionClass.class.getMethod(MockClientActionClass.SINGLE_PARAM_ACTION_METHOD_NAME, IdentifiedUser.class);

    String actionClassName = MockClientActionClass.class.getSimpleName();

    //when
    ClientActionInformation actionInformation = clientActionResolver.prepareActionInformation(actionClassName, method);

    //then
    assertEquals(MockClientActionClass.SINGLE_PARAM_ACTION_NAME, actionInformation.getName());
    PropertySerialization serialization = (PropertySerialization) actionInformation.getSerialization();
    PropertySerialization payloadSerialization = serialization.getPropertySerialization(BristleMessage.PAYLOAD_PROPERTY_NAME);
    assertNotNull(payloadSerialization);
    assertEquals(PropertyType.BEAN, payloadSerialization.getPropertyType());
  }


}
