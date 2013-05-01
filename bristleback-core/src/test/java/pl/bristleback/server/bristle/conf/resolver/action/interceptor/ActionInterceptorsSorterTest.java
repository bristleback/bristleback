package pl.bristleback.server.bristle.conf.resolver.action.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.annotation.Order;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.action.ActionInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorInformation;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptors;
import pl.bristleback.server.bristle.action.interceptor.InterceptionProcessContext;
import pl.bristleback.server.bristle.api.action.ActionInterceptor;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActionInterceptorsSorterTest {

  private FirstActionInterceptor firstActionInterceptor = new FirstActionInterceptor();

  private SecondActionInterceptor secondActionInterceptor = new SecondActionInterceptor();

  private OtherActionInterceptor otherActionInterceptor = new OtherActionInterceptor();

  private ActionInterceptorsSorter sorter;

  @Before
  public void setUp() {
    sorter = new ActionInterceptorsSorter();
  }

  @Test
  public void shouldSortActionInterceptors1() {
    // given
    ActionExecutionStage stage = ActionExecutionStage.ACTION_EXECUTION;

    ActionInterceptorInformation firstActionInterceptorInformation = mock(ActionInterceptorInformation.class);
    when(firstActionInterceptorInformation.getInterceptorInstance()).thenReturn(firstActionInterceptor);
    when(firstActionInterceptorInformation.getStages()).thenReturn(new ActionExecutionStage[]{stage});
    InterceptionProcessContext firstActionInterceptorProcessContext = mock(InterceptionProcessContext.class);
    when(firstActionInterceptorProcessContext.getInterceptorInformation()).thenReturn(firstActionInterceptorInformation);

    ActionInterceptorInformation secondActionInterceptorInformation = mock(ActionInterceptorInformation.class);
    when(secondActionInterceptorInformation.getInterceptorInstance()).thenReturn(secondActionInterceptor);
    when(secondActionInterceptorInformation.getStages()).thenReturn(new ActionExecutionStage[]{stage});
    InterceptionProcessContext secondActionInterceptorProcessContext = mock(InterceptionProcessContext.class);
    when(secondActionInterceptorProcessContext.getInterceptorInformation()).thenReturn(secondActionInterceptorInformation);

    ActionInterceptorInformation otherActionInterceptorInformation = mock(ActionInterceptorInformation.class);
    when(otherActionInterceptorInformation.getInterceptorInstance()).thenReturn(otherActionInterceptor);
    when(otherActionInterceptorInformation.getStages()).thenReturn(new ActionExecutionStage[]{stage});
    InterceptionProcessContext otherActionInterceptorProcessContext = mock(InterceptionProcessContext.class);
    when(otherActionInterceptorProcessContext.getInterceptorInformation()).thenReturn(otherActionInterceptorInformation);

    List<InterceptionProcessContext> allInterceptors = Arrays.asList(secondActionInterceptorProcessContext, firstActionInterceptorProcessContext, otherActionInterceptorProcessContext);

    // when
    ActionInterceptors actionInterceptors = sorter.sortInterceptors(allInterceptors);

    // then
    List<InterceptionProcessContext> processContextList = actionInterceptors.getInterceptorsForStage(stage);
    assertThat(processContextList)
      .isNotNull()
      .hasSize(3)
      .containsExactly(firstActionInterceptorProcessContext, secondActionInterceptorProcessContext, otherActionInterceptorProcessContext);
  }

  @Order(1)
  private class FirstActionInterceptor implements ActionInterceptor<String> {
    @Override
    public void intercept(ActionInformation actionInformation, ActionExecutionContext context, String interceptorContext) {

    }
  }

  @Order(2)
  private class SecondActionInterceptor implements ActionInterceptor<String> {

    @Override
    public void intercept(ActionInformation actionInformation, ActionExecutionContext context, String interceptorContext) {

    }
  }

  private class OtherActionInterceptor implements ActionInterceptor<String> {

    @Override
    public void intercept(ActionInformation actionInformation, ActionExecutionContext context, String interceptorContext) {

    }
  }
}
