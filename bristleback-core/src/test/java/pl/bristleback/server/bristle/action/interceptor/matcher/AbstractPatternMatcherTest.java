package pl.bristleback.server.bristle.action.interceptor.matcher;

import org.junit.Ignore;

@Ignore
public class AbstractPatternMatcherTest<T extends AbstractPatternMatcher> extends AbstractMatcherTest {

  protected T matcher;

  protected void setUpMatcher(T matcher, String... patterns) {
    this.matcher = matcher;
    matcher.setPatterns(patterns);
    matcher.parsePatterns();
  }
}
