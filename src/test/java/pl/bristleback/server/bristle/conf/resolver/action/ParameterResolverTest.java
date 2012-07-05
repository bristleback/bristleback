package pl.bristleback.server.bristle.conf.resolver.action;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.base.users.DefaultUser;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.action.SimpleActionClass;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class ParameterResolverTest {

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private ParameterResolver parameterResolver;

  @Before
  public void setUp() {
    parameterResolver = mockBeansFactory.getFrameworkBean("parameterResolver", ParameterResolver.class);
  }

  @Test
  public void testDefault() throws NoSuchMethodException {
    //given
    Method defaultAction = SimpleActionClass.class.getMethod("executeDefault", DefaultUser.class, String.class);
    Type[] parameters = defaultAction.getGenericParameterTypes();
    Type userParameter = parameters[0];
    Annotation[] userParameterAnnotations = defaultAction.getParameterAnnotations()[0];

    //when
    ActionParameterInformation parameterInformation = parameterResolver.prepareActionParameter(userParameter, userParameterAnnotations);

    //then
    assertFalse(parameterInformation.getExtractor().isDeserializationRequired());
  }

  @Test
  public void parameterWithBindAnnotation() throws NoSuchMethodException {
    //given
    Method defaultAction = SimpleActionClass.class.getMethod("nonDefaultAction", String.class);
    Type[] parameters = defaultAction.getGenericParameterTypes();
    Type connectorParameter = parameters[0];
    Annotation[] connectorParameterAnnotations = defaultAction.getParameterAnnotations()[0];

    //when
    ActionParameterInformation parameterInformation = parameterResolver.prepareActionParameter(connectorParameter, connectorParameterAnnotations);

    //then
    assertTrue(parameterInformation.getExtractor().isDeserializationRequired());
  }
}
