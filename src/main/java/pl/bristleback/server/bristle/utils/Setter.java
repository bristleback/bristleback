package pl.bristleback.server.bristle.utils;


import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class that holds information about object setter.
 * Contains information about setter method and field name.
 * <p/>
 * Created on: 2011-02-23 11:40:30 <br/>
 *
 * @author Wojciech Niemiec
 */
public class Setter {
  private static Logger log = Logger.getLogger(Setter.class.getName());

  private String fieldName;
  private Method setterMethod;

  /**
   * Creates a new Setter object with complete information.
   *
   * @param fieldName    name of the field.
   * @param setterMethod getter method.
   */
  public Setter(String fieldName, Method setterMethod) {
    this.fieldName = fieldName;
    this.setterMethod = setterMethod;
  }

  /**
   * Invokes getter method on the object given as parameter.
   *
   * @param bean  the object the underlying method is invoked from.
   * @param value value of field.
   * @throws IllegalAccessException    exception thrown for further processing.
   * @throws InvocationTargetException exception thrown for further processing.
   */
  public void invokeWithoutCheck(Object bean, Object value) throws IllegalAccessException, InvocationTargetException {
    setterMethod.invoke(bean, value);
  }

  /**
   * Gets the type of field.
   *
   * @return type of object set by this setter method.
   */
  public Class getFieldType() {
    return setterMethod.getParameterTypes()[0];
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
  public Method getSetterMethod() {
    return setterMethod;
  }
}
