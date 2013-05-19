package pl.bristleback.server.bristle.utils;

import org.junit.Test;
import pl.bristleback.server.bristle.api.annotations.Intercept;
import pl.bristleback.server.bristle.api.annotations.Authorized;
import pl.bristleback.server.mock.action.SimpleActionClass;

import java.lang.reflect.Method;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class AnnotationUtilsTest {

  @Test
  public void testFindNestedAnnotationsInClass() throws Exception {
    //given

    //when
    List<Intercept> interceptAnnotations = AnnotationUtils.findNestedAnnotations(SimpleActionClass.class, Intercept.class);

    //then
    assertEquals(1, interceptAnnotations.size());
  }

  @Test
  public void testFindNestedAnnotationsInMethod() throws Exception {
    //given
    Method action = SimpleActionClass.class.getDeclaredMethod("nonDefaultAction", String.class);

    //when
    List<Intercept> interceptAnnotations = AnnotationUtils.findNestedAnnotations(action, Intercept.class);

    //then
    assertEquals(1, interceptAnnotations.size());
  }

  @Test
  public void testFindAnnotation() throws Exception {
    //given
    Method action = SimpleActionClass.class.getDeclaredMethod("nonDefaultAction", String.class);
    Authorized authorizedAnnotation = action.getAnnotation(Authorized.class);

    //when
    Intercept nestedAnnotation = AnnotationUtils.findAnnotation(authorizedAnnotation, Intercept.class);

    //then
    assertNotNull(nestedAnnotation);
  }
}
