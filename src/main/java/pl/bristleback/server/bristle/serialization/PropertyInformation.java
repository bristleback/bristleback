package pl.bristleback.server.bristle.serialization;

import org.apache.log4j.Logger;

import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-26 12:42:24 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PropertyInformation {
  private static Logger log = Logger.getLogger(PropertyInformation.class.getName());

  private boolean detailedErrors;
  private boolean required;
  private boolean skipped;
  private String name;

  private Type type;
  private Class elementClass;

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public boolean isSkipped() {
    return skipped;
  }

  public void setSkipped(boolean skipped) {
    this.skipped = skipped;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Class getElementClass() {
    return elementClass;
  }

  public void setElementClass(Class elementClass) {
    this.elementClass = elementClass;
  }

  public boolean isDetailedErrors() {
    return detailedErrors;
  }

  public void setDetailedErrors(boolean detailedErrors) {
    this.detailedErrors = detailedErrors;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }
}