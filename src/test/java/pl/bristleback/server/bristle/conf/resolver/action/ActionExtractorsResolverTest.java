package pl.bristleback.server.bristle.conf.resolver.action;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.action.extractor.ActionExtractorsContainer;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.bristle.engine.base.users.DefaultUser;
import pl.bristleback.server.mock.beans.NonDefaultSerializedMockBean;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.resolver.SimpleMockParameterExtractor;

import javax.inject.Inject;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-13 12:56:26 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class ActionExtractorsResolverTest {
  private static Logger log = Logger.getLogger(ActionExtractorsResolverTest.class.getName());

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private ActionExtractorsResolver actionExtractorsResolver;

  @Before
  public void setUp() {
    actionExtractorsResolver = mockBeansFactory.getFrameworkBean("actionExtractorsResolver", ActionExtractorsResolver.class);
  }

  @Test
  public void resolveBuiltInParameterExtractors() throws Exception {
    //given

    //when
    ActionExtractorsContainer extractorsContainer = actionExtractorsResolver.resolveParameterExtractors();

    //then
    ActionParameterExtractor defaultObjectExtractor = extractorsContainer.getParameterExtractor(Object.class);
    assertNotNull(defaultObjectExtractor);
    assertTrue(defaultObjectExtractor.isDeserializationRequired());

    ActionParameterExtractor connectorExtractor = extractorsContainer.getParameterExtractor(DefaultUser.class);
    assertNotNull(connectorExtractor);
    assertFalse(connectorExtractor.isDeserializationRequired());
  }

  @Test
  public void resolveCustomParameterExtractor() throws Exception {
    //when
    ActionExtractorsContainer extractorsContainer = actionExtractorsResolver.resolveParameterExtractors();

    //then
    ActionParameterExtractor customBeanExtractor = extractorsContainer.getParameterExtractor(NonDefaultSerializedMockBean.class);
    assertNotNull(customBeanExtractor);
    assertTrue(customBeanExtractor instanceof SimpleMockParameterExtractor);
    assertTrue(customBeanExtractor.isDeserializationRequired());
  }
}
