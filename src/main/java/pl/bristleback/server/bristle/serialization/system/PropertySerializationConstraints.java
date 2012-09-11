package pl.bristleback.server.bristle.serialization.system;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-15 10:14:19 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PropertySerializationConstraints {

  private boolean required;
  private boolean detailedErrors;

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public boolean requiresDetailedErrors() {
    return detailedErrors;
  }

  public void setDetailedErrors(boolean detailedErrors) {
    this.detailedErrors = detailedErrors;
  }
}
