package pl.bristleback.server.bristle.utils;

import org.apache.log4j.Logger;
import org.junit.Test;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.bristle.api.users.IdentifiedUser;
import pl.bristleback.server.mock.action.client.MockClientActionClass;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

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

  @Test
  public void shouldFindIdenticalAnnotation() throws NoSuchMethodException {
    //given
    Method method = MockClientActionClass.class.getMethod(MockClientActionClass.SIMPLE_ACTION_NAME,
      String.class, VerySimpleMockBean.class, IdentifiedUser.class);
    Annotation[] annotations = method.getParameterAnnotations()[1];

    Class<Bind> bindClass = Bind.class;

    //when
    Bind annotationToFind = ReflectionUtils.findAnnotation(annotations, bindClass);

    //then
    assertNotNull(annotationToFind);
  }

  @Test
  public void shouldNotFindIdenticalAnnotation() throws NoSuchMethodException {
    //given
    Method method = MockClientActionClass.class.getMethod(MockClientActionClass.SIMPLE_ACTION_NAME,
      String.class, VerySimpleMockBean.class, IdentifiedUser.class);
    Annotation[] annotations = method.getParameterAnnotations()[2];

    Class<Bind> bindClass = Bind.class;

    //when
    Bind annotationToFind = ReflectionUtils.findAnnotation(annotations, bindClass);

    //then
    assertNull(annotationToFind);
  }

  class BaseExceptionHandler implements ActionExceptionHandler<RuntimeException> {

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
    public Object handleException(IllegalAccessException e, ActionExecutionContext context) {
      return null;
    }

    @Override
    public ActionExecutionStage[] getHandledStages() {
      return new ActionExecutionStage[0];
    }
  }
}

