package pl.bristleback.server.bristle.utils;

import org.apache.log4j.Logger;
import org.junit.Test;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;

import java.lang.reflect.Type;

import static junit.framework.Assert.assertEquals;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-05 19:01:49 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ReflectionUtilsTest {
  private static Logger log = Logger.getLogger(ReflectionUtilsTest.class.getName());

  @Test
  public void getParameterTypesCorrectlyDirectImplementation() {
    //when
    Type[] parameterTypes = ReflectionUtils.getParameterTypes(BaseExceptionHandler.class, ActionExceptionHandler.class);

    //then
    assertEquals(RuntimeException.class, parameterTypes[0]);
  }

  @Test
  public void getParameterTypesCorrectlySubclassImplementation() {
    //when
    Type[] parameterTypes = ReflectionUtils.getParameterTypes(SubExceptionHandler.class, ActionExceptionHandler.class);

    //then
    assertEquals(RuntimeException.class, parameterTypes[0]);
  }

  /*@Test*/ //todo-wojtek fix it

  public void getParameterTypesCorrectlySubclassImplementationWithTypedAbstractClass() {
    //when
    Type[] parameterTypes = ReflectionUtils.getParameterTypes(SubExceptionHandler2.class, ActionExceptionHandler.class);

    //then
    assertEquals(RuntimeException.class, parameterTypes[0]);
  }

  class BaseExceptionHandler implements ActionExceptionHandler<RuntimeException> {
    @Override
    public void init(BristlebackConfig configuration) {

    }

    @Override
    public Object handleException(RuntimeException e, ActionExecutionContext context) {
      return null;
    }

    @Override
    public ActionExecutionStage[] getHandledStages() {
      return new ActionExecutionStage[0];
    }
  }

  class SubExceptionHandler extends BaseExceptionHandler {

  }

  abstract class AbstractExceptionHandler<T extends Exception> implements ActionExceptionHandler<T> {

  }

  class SubExceptionHandler2 extends AbstractExceptionHandler<IllegalAccessException> {
    @Override
    public void init(BristlebackConfig configuration) {

    }

    @Override
    public Object handleException(IllegalAccessException e, ActionExecutionContext context) {
      return null;
    }

    @Override
    public ActionExecutionStage[] getHandledStages() {
      return new ActionExecutionStage[0];
    }
  }
}

