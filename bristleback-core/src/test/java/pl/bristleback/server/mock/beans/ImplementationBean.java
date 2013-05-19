package pl.bristleback.server.mock.beans;

public class ImplementationBean extends AbstractBean<VerySimpleMockBean> {

  private VerySimpleMockBean object;

  private String additionalField;

  public VerySimpleMockBean getObject() {
    return object;
  }

  public void setObject(VerySimpleMockBean object) {
    this.object = object;
  }

  public String getAdditionalField() {
    return additionalField;
  }

  public void setAdditionalField(String additionalField) {
    this.additionalField = additionalField;
  }
}
