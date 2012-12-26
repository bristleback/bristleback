package pl.bristleback.server.bristle.utils;

import org.apache.commons.lang.StringUtils;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-14 21:25:43 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class PropertyUtils {

  private static final String SETTER_PREFIX = "set";
  private static final String GETTER_PREFIX = "get";
  private static final String BOOL_PREFIX = "is";

  private PropertyUtils() {
    throw new UnsupportedOperationException();
  }

  public static List<PropertyAccess> getClassProperties(Class ownerClass, boolean includeSuperFields, String... skippedProperties) {
    return getClassProperties(ownerClass, includeSuperFields, Arrays.asList(skippedProperties));
  }

  public static List<PropertyAccess> getClassProperties(Class ownerClass, boolean includeSuperFields, List<String> skippedProperties) {
    List<PropertyAccess> classProperties = new ArrayList<PropertyAccess>();

    List<Field> fields = getPrivateFields(ownerClass, includeSuperFields, skippedProperties);
    List<Getter> getters = getGetterMethods(ownerClass, includeSuperFields, skippedProperties);
    List<Setter> setters = getSetterMethods(ownerClass, includeSuperFields, skippedProperties);

    for (Field field : fields) {
      Getter getter = getGetter(field.getName(), getters);
      Setter setter = getSetter(field.getName(), setters);
      if (getter != null || setter != null) {
        PropertyAccess propertyAccess = new PropertyAccess(field);
        propertyAccess.setPropertyGetter(getter);
        propertyAccess.setPropertySetter(setter);
        classProperties.add(propertyAccess);
      }

    }

    return classProperties;
  }

  private static Setter getSetter(String fieldName, List<Setter> setters) {
    for (Setter setter : setters) {
      if (setter.getFieldName().equals(fieldName)) {
        return setter;
      }
    }
    return null;
  }

  private static Getter getGetter(String fieldName, List<Getter> getters) {
    for (Getter getter : getters) {
      if (getter.getFieldName().equals(fieldName)) {
        return getter;
      }
    }
    return null;
  }

  private static List<Field> getPrivateFields(Class ownerClass, boolean includeSuperFields, List<String> skippedProperties) {
    List<Field> fields = new ArrayList<Field>();

    Class currentOwnerClass = ownerClass;

    while (currentOwnerClass != null && !currentOwnerClass.equals(Object.class) && !currentOwnerClass.isInterface()) {
      for (Field field : currentOwnerClass.getDeclaredFields()) {
        if (isPrivateField(field) && !isPropertySkipped(field.getName(), skippedProperties)) {
          fields.add(field);
        }
      }

      if (!includeSuperFields) {
        return fields;
      }
      currentOwnerClass = currentOwnerClass.getSuperclass();
    }

    return fields;
  }

  private static boolean isPrivateField(Field field) {
    return !Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers());
  }

  /**
   * Retrieves and wraps getter method of given class.
   *
   * @param clazz              processed class.
   * @param includeSuperFields if set to true, fields of parent class will be included.
   * @param skippedProperties  properties that should not be included.
   * @return list of getter methods wrapped into {@link pl.bristleback.server.bristle.utils.Getter} objects.
   */
  public static List<Getter> getGetterMethods(Class clazz, boolean includeSuperFields, String... skippedProperties) {
    return getGetterMethods(clazz, includeSuperFields, Arrays.asList(skippedProperties));
  }

  /**
   * Retrieves and wraps getter method of given class.
   *
   * @param clazz              processed class.
   * @param includeSuperFields if set to true, fields of parent class will be included.
   * @param skippedProperties  properties that should not be included.
   * @return list of getter methods wrapped into {@link pl.bristleback.server.bristle.utils.Getter} objects.
   */
  public static List<Getter> getGetterMethods(Class clazz, boolean includeSuperFields, List<String> skippedProperties) {
    List<Getter> getters = new ArrayList<Getter>();
    Method[] methods = clazz.getMethods();
    Getter getter;
    for (Method method : methods) {
      if (method.getParameterTypes().length > 0) {
        // skip this method because it is not getter
        continue;
      }
      if (!includeSuperFields && isMethodInherited(clazz, method)) {
        continue;
      }
      String propertyName = extractFieldNameFromGetter(method);
      if (!org.apache.commons.lang.StringUtils.EMPTY.equals(propertyName)) {
        getter = new Getter(propertyName, method);
        if (!isPropertySkipped(getter.getFieldName(), skippedProperties)) {
          getters.add(getter);
        }
      }
    }
    return getters;
  }

  /**
   * Retrieves and wraps setter method of given class.
   *
   * @param clazz              processed class.
   * @param includeSuperFields if set to true, fields of parent class will be included.
   * @param skippedProperties  properties that should not be included.
   * @return list of setter methods wrapped into {@link pl.bristleback.server.bristle.utils.Setter} objects.
   */
  public static List<Setter> getSetterMethods(Class clazz, boolean includeSuperFields, List<String> skippedProperties) {
    List<Setter> setters = new ArrayList<Setter>();
    Method[] methods = clazz.getMethods();
    Setter setter;
    for (Method method : methods) {
      if (method.getParameterTypes().length != 1) {
        // skip this method because it is not getter
        continue;
      }
      if (!includeSuperFields && isMethodInherited(clazz, method)) {
        continue;
      }
      String propertyName = extractFieldNameFromSetter(method);
      if (!org.apache.commons.lang.StringUtils.EMPTY.equals(propertyName)) {
        setter = new Setter(propertyName, method);
        if (!isPropertySkipped(setter.getFieldName(), skippedProperties)) {
          setters.add(setter);
        }
      }
    }
    return setters;
  }

  private static String extractFieldNameFromGetter(Method method) {
    String methodName = method.getName();
    if (methodName.equals("getClass")) {
      return org.apache.commons.lang.StringUtils.EMPTY;
    }
    return extractFieldName(methodName, GETTER_PREFIX, BOOL_PREFIX);
  }

  private static String extractFieldNameFromSetter(Method method) {
    String methodName = method.getName();
    return extractFieldName(methodName, SETTER_PREFIX);
  }

  private static String extractFieldName(String methodName, String... allowedPrefixes) {
    for (String allowedPrefix : allowedPrefixes) {
      if (methodName.startsWith(allowedPrefix)) {
        return methodName.substring(allowedPrefix.length(), allowedPrefix.length() + 1).toLowerCase() + methodName.substring(allowedPrefix.length() + 1);
      }
    }
    return StringUtils.EMPTY;
  }

  private static boolean isPropertySkipped(String propertyName, List<String> skippedProperties) {
    for (String skippedProperty : skippedProperties) {
      if (skippedProperty.equalsIgnoreCase(propertyName)) {
        return true;
      }
    }
    return false;
  }

  public static List<Method> getMethodsAnnotatedWith(Class owner, Class<? extends Annotation> annotationClass, boolean includeSuper) {
    List<Method> selectedMethods = new ArrayList<Method>();
    Method[] allMethods = owner.getMethods();
    for (Method method : allMethods) {
      if (!includeSuper && isMethodInherited(owner, method)) {
        continue;
      }
      if (method.isAnnotationPresent(annotationClass)) {
        selectedMethods.add(method);
      }
    }

    return selectedMethods;
  }

  private static boolean isMethodInherited(Class clazz, Method method) {
    return method.getDeclaringClass() != clazz;
  }

  public static Field getDeclaredField(Class clazz, String fieldName) {
    try {
      return clazz.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      throw new BristleInitializationException("This should never happen.", e);
    }
  }

  public static Type getDeclaredFieldType(Class clazz, String fieldName) {
    return getDeclaredField(clazz, fieldName).getGenericType();
  }
}
