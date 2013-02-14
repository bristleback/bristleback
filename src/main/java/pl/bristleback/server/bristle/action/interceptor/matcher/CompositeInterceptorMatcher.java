package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a convenient logical interceptor matcher, for general usage.
 * It allows gather two or more interceptor matcher objects using several logical operations.
 * Logical operations are defined in {@link LogicalOperation} class.
 * <p/>
 * Created on: 14.02.13 18:34 <br/>
 *
 * @author Wojciech Niemiec
 * @see LogicalOperation
 */
public class CompositeInterceptorMatcher implements ActionInterceptorMatcher {

  private LogicalOperation operation = LogicalOperation.AND;

  private List<ActionInterceptorMatcher> matcherList = new ArrayList<ActionInterceptorMatcher>();

  public CompositeInterceptorMatcher() {
  }

  public CompositeInterceptorMatcher(ActionInterceptorMatcher... matcherArray) {
    this.matcherList = Arrays.asList(matcherArray);
  }

  public CompositeInterceptorMatcher(List<ActionInterceptorMatcher> matcherList) {
    this.matcherList = matcherList;
  }

  public CompositeInterceptorMatcher(LogicalOperation operation, List<ActionInterceptorMatcher> matcherList) {
    this.operation = operation;
    this.matcherList = matcherList;
  }

  public CompositeInterceptorMatcher(LogicalOperation operation, ActionInterceptorMatcher... matcherArray) {
    this.operation = operation;
    this.matcherList = Arrays.asList(matcherArray);
  }

  @Override
  public boolean isInterceptorApplicable(ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor) {
    return operation.operateWith(matcherList, actionInformation, interceptor);
  }

  public void setMatcherArray(ActionInterceptorMatcher... matcherArray) {
    matcherList = Arrays.asList(matcherArray);
  }

  public void setOperation(LogicalOperation operation) {
    this.operation = operation;
  }

  public void setMatcherList(List<ActionInterceptorMatcher> matcherList) {
    this.matcherList = matcherList;
  }
}
