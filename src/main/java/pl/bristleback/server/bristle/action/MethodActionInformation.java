package pl.bristleback.server.bristle.action;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 12:52:57 <br/>
 *
 * @author Wojciech Niemiec
 */
public class MethodActionInformation extends AbstractActionInformation<Object> {
  private static Logger log = Logger.getLogger(MethodActionInformation.class.getName());

  private Method method;

  public MethodActionInformation(String name, Method action) {
    super(name);
    this.method = action;
  }

  @Override
  public Object execute(Object actionClass, Object[] parameters) throws Exception {
    return method.invoke(actionClass, parameters);
  }
}