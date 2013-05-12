package pl.bristleback.server.bristle.conf.resolver.serialization;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.serialization.system.SerializationInput;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.mock.action.SimpleActionClass;
import pl.bristleback.server.mock.beans.SimpleMockBean;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;

import javax.inject.Inject;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertTrue;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-13 18:21:37 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class SerializationInputResolverTest {

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private SerializationInputResolver serializationInputResolver;

  @Before
  public void setUp() throws Exception {
    serializationInputResolver =
      mockBeansFactory.getFrameworkBean("serializationInputResolver", SerializationInputResolver.class);
  }

  @Test
  public void correctlyResolvedUsingBindAnnotationRawType() throws Exception {
    //given
    Method defaultAction = SimpleActionClass.class.getMethod("nonDefaultAction", String.class);
    Bind bindAnnotation = (Bind) defaultAction.getParameterAnnotations()[0][0];
    SerializationInput serializationInput = serializationInputResolver.resolveInputInformation(bindAnnotation);

    assertTrue(serializationInput.containsNonDefaultProperties());
    assertTrue(serializationInput.getPropertyInformation().isRequired());
  }

  @Test
  public void correctlyResolvedUsingBindAnnotationWithNonDefaultProperties() throws Exception {
    //given
    Method defaultAction = SimpleActionClass.class.getMethod("nonDefaultActionWithBind", SimpleMockBean.class);
    Bind bindAnnotation = (Bind) defaultAction.getParameterAnnotations()[0][0];
    SerializationInput serializationInput = serializationInputResolver.resolveInputInformation(bindAnnotation);

    assertTrue(serializationInput.containsNonDefaultProperties());
    assertTrue(serializationInput.getNonDefaultProperties().get(SimpleMockBean.RAW_INT_PROPERTY).getPropertyInformation().isRequired());

  }
}
