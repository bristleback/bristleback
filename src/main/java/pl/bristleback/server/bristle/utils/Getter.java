package pl.bristleback.server.bristle.utils;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.exceptions.BristleRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-02 12:30:11 <br/>
 *
 * @author Wojciech Niemiec
 */
public class Getter {
  private static Logger log = Logger.getLogger(Getter.class.getName());

  private String fieldName;
  private Method getterMethod;

  /**
   * Creates a new Getter object with complete information.
   *
   * @param fieldName    name of the field.
   * @param getterMethod getter method.
   */
  public Getter(String fieldName, Method getterMethod) {
    this.fieldName = fieldName;
    this.getterMethod = getterMethod;
  }

  /**
   * Invokes getter method on the object given as parameter.
   * Any exception while invoking method will cause {@link BristleRuntimeException} to throw.
   *
   * @param bean the object the underlying method is invoked from.
   * @return the result of invoking getter method.
   */
  public Object invoke(Object bean) {
    try {
      return getterMethod.invoke(bean);
    } catch (IllegalAccessException e) {
      throw new BristleRuntimeException(e.getMessage(), e);
    } catch (InvocationTargetException e) {
      throw new BristleRuntimeException(e.getMessage(), e);
    }
  }

  /**
   * Invokes getter method on the object given as parameter.
   * Any exception while invoking method will cause {@link BristleRuntimeException} to throw.
   *
   * @param bean the object the underlying method is invoked from.
   * @return the result of invoking getter method.
   * @throws Exception exceptions related with invoking method using reflection.
   */
  public Object invokeWithoutCheck(Object bean) throws Exception {
    return getterMethod.invoke(bean);
  }

  /**
   * Type of object returned by getter method.
   *
   * @return type of object returned by getter method.
   */
  public Class getReturnType() {
    return getterMethod.getReturnType();
  }

  /**
   * Name of the returned field name.
   *
   * @return name of the returned field name.
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * Getter method.
   *
   * @return getter method.
   */
  public Method getGetterMethod() {
    return getterMethod;
  }

  public Type getGenericReturnType() {
    return getterMethod.getGenericReturnType();
  }
}