package pl.bristleback.server.bristle.utils;

import org.apache.log4j.Logger;
import org.junit.Test;
import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;
import pl.bristleback.server.bristle.api.action.ActionExceptionHandler;
import pl.bristleback.server.bristle.api.annotations.Ignore;
import pl.bristleback.server.bristle.api.users.UserContext;
import pl.bristleback.server.bristle.BristleRuntimeException;
import pl.bristleback.server.mock.action.client.MockClientActionClass;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

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

  @Test
  public void getParameterTypesCorrectlySubclassImplementationWithTypedAbstractClass() {
    //when
    Type[] parameterTypes = ReflectionUtils.getParameterTypes(SubExceptionHandler2.class, ActionExceptionHandler.class);

    //then
    assertEquals(IllegalAccessException.class, parameterTypes[0]);
  }

  @Test
  public void getParameterTypesCorrectlySubclassImplementationWithTwoTypedAbstractClasses() {
    //when
    Type[] parameterTypes = ReflectionUtils.getParameterTypes(SubExceptionHandlerFromLessAbstract.class, ActionExceptionHandler.class);

    //then
    assertEquals(BristleRuntimeException.class, parameterTypes[0]);
  }

  @Test
  public void shouldFindIdenticalAnnotation() throws NoSuchMethodException {
    //given
    Method method = MockClientActionClass.class.getMethod(MockClientActionClass.SIMPLE_ACTION_NAME,
      String.class, VerySimpleMockBean.class, UserContext.class);
    Annotation[] annotations = method.getParameterAnnotations()[2];

    //when
    Ignore annotationToFind = ReflectionUtils.findAnnotation(annotations, Ignore.class);

    //then
    assertNotNull(annotationToFind);
  }

  @Test
  public void shouldNotFindIdenticalAnnotation() throws NoSuchMethodException {
    //given
    Method method = MockClientActionClass.class.getMethod(MockClientActionClass.SIMPLE_ACTION_NAME,
      String.class, VerySimpleMockBean.class, UserContext.class);
    Annotation[] annotations = method.getParameterAnnotations()[1];

    //when
    Ignore annotationToFind = ReflectionUtils.findAnnotation(annotations, Ignore.class);

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

  abstract class LessAbstractExceptionHandler<X extends Exception> extends AbstractExceptionHandler<X> {

    public Object handleException(X exception, ActionExecutionContext context) {
      return null;
    }
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

  class SubExceptionHandlerFromLessAbstract extends LessAbstractExceptionHandler<BristleRuntimeException> {

    @Override
    public ActionExecutionStage[] getHandledStages() {
      return new ActionExecutionStage[0];
    }
  }

}

