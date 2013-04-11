package pl.bristleback.server.bristle.action.interceptor.matcher;

import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;
import pl.bristleback.server.bristle.api.action.ActionInterceptorMatcher;

import java.util.List;

/**
 * Logical operations used in {@link CompositeInterceptorMatcher} class.
 * <p/>
 * Created on: 14.02.13 19:25 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum LogicalOperation {

  AND {
    @Override
    public boolean operateWith(List<ActionInterceptorMatcher> matcherList, ActionInformation actionInformation,
                               Class<? extends ActionInterceptor> interceptor) {
      for (ActionInterceptorMatcher actionInterceptorMatcher : matcherList) {
        boolean result = actionInterceptorMatcher.isInterceptorApplicable(actionInformation, interceptor);
        if (!result) {
          return false;
        }
      }
      return true;
    }
  },
  OR {
    @Override
    public boolean operateWith(List<ActionInterceptorMatcher> matcherList, ActionInformation actionInformation,
                               Class<? extends ActionInterceptor> interceptor) {
      for (ActionInterceptorMatcher actionInterceptorMatcher : matcherList) {
        boolean result = actionInterceptorMatcher.isInterceptorApplicable(actionInformation, interceptor);
        if (result) {
          return true;
        }
      }
      return false;
    }
  },
  NOT {
    @Override
    public boolean operateWith(List<ActionInterceptorMatcher> matcherList, ActionInformation actionInformation,
                               Class<? extends ActionInterceptor> interceptor) {
      return !AND.operateWith(matcherList, actionInformation, interceptor);
    }
  };

  public abstract boolean operateWith(List<ActionInterceptorMatcher> matcherList,
                                      ActionInformation actionInformation, Class<? extends ActionInterceptor> interceptor);
}
