package pl.bristleback.server.bristle.serialization.system;

import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-02 14:19:59 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClassTypeParameter {
  private static Logger log = Logger.getLogger(ClassTypeParameter.class.getName());

  private TypeVariable parameterName;

  private Type parameterType;

  public ClassTypeParameter(TypeVariable parameterName, Type parameterType) {
    this.parameterName = parameterName;
    this.parameterType = parameterType;
  }

  public TypeVariable getParameterName() {
    return parameterName;
  }

  public Type getParameterType() {
    return parameterType;
  }
}
