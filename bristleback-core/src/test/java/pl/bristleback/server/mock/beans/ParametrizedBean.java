package pl.bristleback.server.mock.beans;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-03 10:39:34 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ParametrizedBean<T> {

  private static Logger log = Logger.getLogger(ParametrizedBean.class.getName());


  private T parametrizedField1;

  private List<T> parametrizedList;

  private T[] parametrizedArray;

  private Map<String, T> parametrizedMap;

  public T getParametrizedField1() {
    return parametrizedField1;
  }

  public void setParametrizedField1(T parametrizedField1) {
    this.parametrizedField1 = parametrizedField1;
  }

  public List<T> getParametrizedList() {
    return parametrizedList;
  }

  public void setParametrizedList(List<T> parametrizedList) {
    this.parametrizedList = parametrizedList;
  }

  public T[] getParametrizedArray() {
    return parametrizedArray;
  }

  public void setParametrizedArray(T[] parametrizedArray) {
    this.parametrizedArray = parametrizedArray;
  }

  public Map<String, T> getParametrizedMap() {
    return parametrizedMap;
  }

  public void setParametrizedMap(Map<String, T> parametrizedMap) {
    this.parametrizedMap = parametrizedMap;
  }
}
