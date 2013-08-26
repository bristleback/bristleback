package pl.bristleback.server.mock.beans;

public class VerySimpleMockBean {

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
