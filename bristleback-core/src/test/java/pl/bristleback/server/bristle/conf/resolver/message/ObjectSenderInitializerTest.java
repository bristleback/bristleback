package pl.bristleback.server.bristle.conf.resolver.message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.message.ConditionObjectSender;
import pl.bristleback.server.mock.action.SimpleActionClass;
import pl.bristleback.server.mock.beans.MockBean;
import pl.bristleback.server.mock.beans.SimpleMockBean;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;

import static junit.framework.Assert.assertNotNull;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-11-23 10:17:21 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class ObjectSenderInitializerTest {

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private ObjectSenderInitializer objectSenderInitializer;

  @Inject
  @Named("system.sender.condition")
  private ConditionObjectSender objectSender;

  @Before
  public void setUp() {
    objectSenderInitializer = mockBeansFactory.getFrameworkBean("objectSenderInitializer", ObjectSenderInitializer.class);
  }

  @Test
  public void shouldInitObjectSenderWithBothDefaultAndNonDefaultSerialization() throws NoSuchFieldException {
    //given
    Field field = SimpleActionClass.class.getDeclaredField("conditionObjectSender");
    objectSender.setField(field);
    //when
    objectSenderInitializer.initObjectSender(objectSender);

    //then
    assertNotNull(objectSender.getLocalSerializations().getSerialization(MockBean.class));
    assertNotNull(objectSender.getLocalSerializations().getSerialization(SimpleMockBean.class));
  }
}
