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

  public PropertyAccess(Field propertyField) {
    this.propertyField = propertyField;
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

  public void setPropertyGetter(Getter propertyGetter) {
    this.propertyGetter = propertyGetter;
  }

  public void setPropertySetter(Setter propertySetter) {
    this.propertySetter = propertySetter;
  }

  public boolean isReadable() {
    return propertyGetter != null;
  }

  public boolean isWritable() {
    return propertySetter != null;
  }
}
