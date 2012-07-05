package pl.bristleback.server.mock.beans;

public class SimpleMockBean {

  public static final String RAW_STRING_PROPERTY = "property2";
  public static final String RAW_INT_PROPERTY = "property1";

  private int property1;
  private String property2;

  public int getProperty1() {
    return property1;
  }

  public void setProperty1(int property1) {
    this.property1 = property1;
  }

  public String getProperty2() {
    return property2;
  }

  public void setProperty2(String property2) {
    this.property2 = property2;
  }
}
