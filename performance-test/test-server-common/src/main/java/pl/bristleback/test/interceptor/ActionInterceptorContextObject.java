package pl.bristleback.test.interceptor;

public class ActionInterceptorContextObject {

  private int actionAnnotationsCount;

  public ActionInterceptorContextObject(int actionAnnotationsCount) {
    this.actionAnnotationsCount = actionAnnotationsCount;
  }

  public int getActionAnnotationsCount() {
    return actionAnnotationsCount;
  }
}
