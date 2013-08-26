package pl.bristleback.server.mock.beans;

import org.apache.log4j.Logger;

public class VerySimpleMockBean {

  private static Logger log = Logger.getLogger(VerySimpleMockBean.class.getName());

  private int simpleField;

  public VerySimpleMockBean() {
  }

  public VerySimpleMockBean(int simpleField) {
    this.simpleField = simpleField;
  }

  public int getSimpleField() {
    return simpleField;
  }

  public void setSimpleField(int simpleField) {
    this.simpleField = simpleField;
  }
}
