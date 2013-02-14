package pl.bristleback.server.bristle.action.interceptor.matcher;

import org.junit.Before;
import org.junit.Ignore;
import pl.bristleback.server.bristle.action.ActionsContainer;
import pl.bristleback.server.bristle.conf.resolver.action.ActionClassesResolver;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;

import javax.inject.Inject;

@Ignore
public class AbstractPatternMatcherTest<T extends AbstractPatternMatcher> {

  protected T matcher;

  protected ActionsContainer actionsContainer;

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  @Before
  public void setUp() {
    if (actionsContainer == null) {
      ActionClassesResolver actionClassesResolver = mockBeansFactory.getFrameworkBean("actionClassesResolver", ActionClassesResolver.class);
      actionsContainer = actionClassesResolver.resolve();
    }
  }

  protected void setUpMatcher(T matcher, String... patterns) {
    this.matcher = matcher;
    matcher.setPatterns(patterns);
    matcher.parsePatterns();
  }

  public ActionsContainer getActionsContainer() {
    return actionsContainer;
  }
}
