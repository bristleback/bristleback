package pl.bristleback.server.bristle.action.interceptor.matcher;

import org.junit.Test;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;
import pl.bristleback.server.mock.action.MockActionInterceptor;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CompositeInterceptorMatcherTest {

  @Test
  public void shouldInterceptorMatchWithAndOperator() throws Exception {
    //given
    ActionInformation actionInformation = null;
    CompositeInterceptorMatcher compositeInterceptorMatcher = new CompositeInterceptorMatcher();

    ActionInterceptorMatcher matcher1 = new TrueMatcher();
    ActionInterceptorMatcher matcher2 = new TrueMatcher();
    ActionInterceptorMatcher matcher3 = new TrueMatcher();

    compositeInterceptorMatcher.setMatcherArray(matcher1, matcher2, matcher3);

    //when
    boolean result = compositeInterceptorMatcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertTrue(result);
  }

  @Test
  public void shouldInterceptorNotMatchWithAndOperator() throws Exception {
    //given
    ActionInformation actionInformation = null;
    CompositeInterceptorMatcher compositeInterceptorMatcher = new CompositeInterceptorMatcher();

    ActionInterceptorMatcher matcher1 = new TrueMatcher();
    ActionInterceptorMatcher matcher2 = new FalseMatcher();
    ActionInterceptorMatcher matcher3 = new TrueMatcher();

    compositeInterceptorMatcher.setMatcherArray(matcher1, matcher2, matcher3);

    //when
    boolean result = compositeInterceptorMatcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertFalse(result);
  }

  @Test
  public void shouldInterceptorMatchWithOrOperator() throws Exception {
    //given
    ActionInformation actionInformation = null;

    ActionInterceptorMatcher matcher1 = new FalseMatcher();
    ActionInterceptorMatcher matcher2 = new FalseMatcher();
    ActionInterceptorMatcher matcher3 = new TrueMatcher();

    CompositeInterceptorMatcher compositeInterceptorMatcher =
      new CompositeInterceptorMatcher(LogicalOperation.OR, matcher1, matcher2, matcher3);

    //when
    boolean result = compositeInterceptorMatcher.isInterceptorApplicable(actionInformation,MockActionInterceptor.class);

    //then
    assertTrue(result);
  }

  @Test
  public void shouldInterceptorNotMatchWithOrOperator() throws Exception {
    //given
    ActionInformation actionInformation = null;

    ActionInterceptorMatcher matcher1 = new FalseMatcher();
    ActionInterceptorMatcher matcher2 = new FalseMatcher();
    ActionInterceptorMatcher matcher3 = new FalseMatcher();

    CompositeInterceptorMatcher compositeInterceptorMatcher =
      new CompositeInterceptorMatcher(LogicalOperation.OR, matcher1, matcher2, matcher3);

    //when
    boolean result = compositeInterceptorMatcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertFalse(result);
  }

  @Test
  public void shouldInterceptorMatchWithNotOperator() throws Exception {
    //given
    ActionInformation actionInformation = null;

    ActionInterceptorMatcher matcher1 = new FalseMatcher();
    ActionInterceptorMatcher matcher2 = new TrueMatcher();
    ActionInterceptorMatcher matcher3 = new TrueMatcher();

    CompositeInterceptorMatcher compositeInterceptorMatcher =
      new CompositeInterceptorMatcher(LogicalOperation.NOT, matcher1, matcher2, matcher3);

    //when
    boolean result = compositeInterceptorMatcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertTrue(result);
  }

  @Test
  public void shouldInterceptorNotMatchWithNotOperator() throws Exception {
    //given
    ActionInformation actionInformation = null;

    ActionInterceptorMatcher matcher1 = new TrueMatcher();
    ActionInterceptorMatcher matcher2 = new TrueMatcher();
    ActionInterceptorMatcher matcher3 = new TrueMatcher();

    CompositeInterceptorMatcher compositeInterceptorMatcher =
      new CompositeInterceptorMatcher(LogicalOperation.NOT, matcher1, matcher2, matcher3);

    //when
    boolean result = compositeInterceptorMatcher.isInterceptorApplicable(actionInformation, MockActionInterceptor.class);

    //then
    assertFalse(result);
  }

  private class FalseMatcher implements ActionInterceptorMatcher {

    @Override
    public boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor) {
      return false;
    }
  }

  private class TrueMatcher implements ActionInterceptorMatcher {

    @Override
    public boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor) {
      return true;
    }
  }
}
