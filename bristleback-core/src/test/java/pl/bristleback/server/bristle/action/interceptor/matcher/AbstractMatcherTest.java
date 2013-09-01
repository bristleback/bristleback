package pl.bristleback.server.bristle.action.interceptor.matcher;

import org.junit.Before;
import org.junit.Ignore;
import pl.bristleback.server.bristle.action.ActionsContainer;
import pl.bristleback.server.bristle.conf.resolver.action.ActionClassesResolver;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;

import javax.inject.Inject;

@Ignore
public class AbstractMatcherTest {

  protected ActionsContainer actionsContainer;

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  @Before
  public void setUp() {
    if (actionsContainer == null) {
      actionsContainer = mockBeansFactory.getFrameworkBean("actionsContainer", ActionsContainer.class);
    }
  }

  public ActionsContainer getActionsContainer() {
    return actionsContainer;
  }
}
