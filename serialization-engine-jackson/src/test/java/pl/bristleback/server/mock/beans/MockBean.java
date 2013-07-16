package pl.bristleback.server.mock.beans;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class MockBean {

  private static Logger log = Logger.getLogger(MockBean.class.getName());

  public static final String RAW_STRING_PROPERTY = "property1";

  public static final String RAW_INT_PROPERTY = "property2";

  public static final String RAW_DOUBLE_PROPERTY = "property3";

  public static final String LIST_OF_STRINGS_PROPERTY = "property4";

  public static final String MAP_OF_DOUBLES_PROPERTY = "property5";

  public static final String ARRAY_OF_BEANS_PROPERTY = "arrayOfBeans";

  public static final String SIMPLE_MOCK_BEAN_PROPERTY = "simpleMockBean";

  public static final int FIELDS_COUNT = 7;

  private String property1;

  private int property2;

  private Double property3;

  private List<String> property4;

  private Map<String, Double> property5;

  private SimpleMockBean[] arrayOfBeans;

  private SimpleMockBean simpleMockBean;

  public String getProperty1() {
    return property1;
  }

  public void setProperty1(String property1) {
    this.property1 = property1;
  }

  public int getProperty2() {
    return property2;
  }

  public void setProperty2(int property2) {
    this.property2 = property2;
  }

  public Double getProperty3() {
    return property3;
  }

  public void setProperty3(Double property3) {
    this.property3 = property3;
  }

  public List<String> getProperty4() {
    return property4;
  }

  public void setProperty4(List<String> property4) {
    this.property4 = property4;
  }

  public Map<String, Double> getProperty5() {
    return property5;
  }

  public void setProperty5(Map<String, Double> property5) {
    this.property5 = property5;
  }

  public SimpleMockBean getSimpleMockBean() {
    return simpleMockBean;
  }

  public void setSimpleMockBean(SimpleMockBean simpleMockBean) {
    this.simpleMockBean = simpleMockBean;
  }

  public SimpleMockBean[] getArrayOfBeans() {
    return arrayOfBeans;
  }

  public void setArrayOfBeans(SimpleMockBean[] arrayOfBeans) {
    this.arrayOfBeans = arrayOfBeans;
  }

  @Override
  public String toString() {
    return "MockBean{" +
      "property1='" + property1 + '\'' +
      ", property2=" + property2 +
      ", property3=" + property3 +
      '}';
  }
}