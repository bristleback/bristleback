package pl.bristleback.server.bristle.serialization.system;

import java.text.Format;

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
  private Format format;

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

  public Format getFormat() {
    return format;
  }

  public void setFormat(Format format) {
    this.format = format;
  }
}
