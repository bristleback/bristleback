package pl.bristleback.server.bristle.serialization.system;

import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-08-31 17:00:15 <br/>
 *
 * @author Wojciech Niemiec
 */
public class SerializationInput {

  private Map<String, SerializationInput> nonDefaultProperties = new HashMap<String, SerializationInput>();

  private PropertyInformation propertyInformation;

  private boolean detailedErrors;

  public boolean hasSpecifiedType() {
    return propertyInformation != null && propertyInformation.getType() != null;
  }

  public boolean containsNonDefaultProperties() {
    return propertyInformation != null || !nonDefaultProperties.isEmpty();
  }

  public Map<String, SerializationInput> getNonDefaultProperties() {
    return nonDefaultProperties;
  }

  public PropertyInformation getPropertyInformation() {
    return propertyInformation;
  }

  public void setPropertyInformation(PropertyInformation propertyInformation) {
    this.propertyInformation = propertyInformation;
  }

  public boolean isDetailedErrors() {
    return detailedErrors;
  }

  public void setDetailedErrors(boolean detailedErrors) {
    this.detailedErrors = detailedErrors;
  }
}