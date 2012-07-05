package pl.bristleback.server.bristle.exceptions;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-09 13:37:15 <br/>
 *
 * @author Wojciech Niemiec
 */
public class InstanceResolvingException extends RuntimeException {
  private static Logger log = Logger.getLogger(InstanceResolvingException.class.getName());

  private String className;
  private Class parentClass;

  public InstanceResolvingException(String className, Throwable throwable) {
    super(throwable);
    this.className = className;
  }

  public InstanceResolvingException(String className, Class parentClass) {
    this.className = className;
    this.parentClass = parentClass;
  }

  @Override
  public String getMessage() {
    if (parentClass == null) {
      return "Cannot resolve instance of " + className + ".";
    }
    return "Cannot resolve implementation of " + parentClass + ", given class name: " + className;
  }

  public String getClassName() {
    return className;
  }

  public Class getParentClass() {
    return parentClass;
  }

}
