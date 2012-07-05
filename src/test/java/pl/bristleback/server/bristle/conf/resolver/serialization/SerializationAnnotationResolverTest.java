package pl.bristleback.server.bristle.conf.resolver.serialization;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.Serialize;
import pl.bristleback.server.mock.action.SimpleActionClass;
import pl.bristleback.server.mock.beans.SimpleMockBean;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;

import javax.inject.Inject;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-13 17:28:55 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class SerializationAnnotationResolverTest {
  private static Logger log = Logger.getLogger(SerializationAnnotationResolverTest.class.getName());

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private SerializationAnnotationResolver serializationAnnotationResolver;

  @Before
  public void setUp() {
    serializationAnnotationResolver =
      mockBeansFactory.getFrameworkBean("serializationAnnotationResolver", SerializationAnnotationResolver.class);
  }

  @Test
  public void shouldResolveSerialization() throws Exception {
    //given
    Method defaultAction = SimpleActionClass.class.getMethod("nonDefaultAction", String.class);
    Serialize[] actionAnnotation = defaultAction.getAnnotation(Action.class).response();

    //when
    Object serialization = serializationAnnotationResolver.resolveSerialization(SimpleMockBean.class, actionAnnotation[0]);

    //then
    assertNotNull(serialization);
  }
}
