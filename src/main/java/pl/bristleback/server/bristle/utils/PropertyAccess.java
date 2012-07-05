package pl.bristleback.server.bristle.utils;

import java.lang.reflect.Field;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-14 20:50:40 <br/>
 *
 * @author Wojciech Niemiec
 */
public class PropertyAccess {

  private Field propertyField;
  private Getter propertyGetter;
  private Setter propertySetter;

  public PropertyAccess(Field propertyField, Getter propertyGetter, Setter propertySetter) {
    this.propertyField = propertyField;
    this.propertyGetter = propertyGetter;
    this.propertySetter = propertySetter;
  }

  public Field getPropertyField() {
    return propertyField;
  }

  public String getFieldName() {
    return propertyField.getName();
  }

  public Getter getPropertyGetter() {
    return propertyGetter;
  }

  public Setter getPropertySetter() {
    return propertySetter;
  }
}
