package pl.bristleback.server.bristle.utils;

import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.exceptions.BristleInitializationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-22 10:17:24 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class ReflectionUtils {
  private static Logger log = Logger.getLogger(ReflectionUtils.class.getName());

  private static final Map<Class, Class> PRIMITIVE_TO_WRAPPERS_MAP = new HashMap<Class, Class>();
  private static final Map<Class, Class> WRAPPERS_TO_PRIMITIVE_MAP = new HashMap<Class, Class>();

  public static final int COLLECTION_ELEMENT_PARAMETER_INDEX = 0;
  public static final int MAP_ELEMENT_PARAMETER_INDEX = 1;

  private ReflectionUtils() {
    throw new UnsupportedOperationException();
  }

  static {
    addPrimitiveToWrapperRelation(boolean.class, Boolean.class);
    addPrimitiveToWrapperRelation(char.class, Character.class);
    addPrimitiveToWrapperRelation(int.class, Integer.class);
    addPrimitiveToWrapperRelation(long.class, Long.class);
    addPrimitiveToWrapperRelation(float.class, Float.class);
    addPrimitiveToWrapperRelation(double.class, Double.class);
  }

  private static void addPrimitiveToWrapperRelation(Class primitiveClass, Class wrapperClass) {
    PRIMITIVE_TO_WRAPPERS_MAP.put(primitiveClass, wrapperClass);
    WRAPPERS_TO_PRIMITIVE_MAP.put(wrapperClass, primitiveClass);
  }

  public static boolean isRawType(Object value) {
    if (value == null) {
      return true;
    }
    Class valueClass = value.getClass();
    return isRawClass(valueClass);
  }

  public static boolean isRawClass(Class clazz) {
    return clazz.isPrimitive()
      || clazz == Boolean.class
      || clazz == Integer.class
      || clazz == Long.class
      || clazz == Double.class
      || clazz == Character.class
      || clazz == String.class
      || clazz == BigInteger.class
      || clazz == BigDecimal.class;
  }

  public static Class getWrapperClassForPrimitive(Class primitiveClass) {
    return PRIMITIVE_TO_WRAPPERS_MAP.get(primitiveClass);
  }

  public static Class getPrimitiveForWrapper(Class wrapperClass) {
    return WRAPPERS_TO_PRIMITIVE_MAP.get(wrapperClass);
  }

  public static boolean isContainerClass(Class clazz) {
    return clazz.isArray() || Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
  }

  public static Type[] getParameterTypes(Class implementation, Class parametrizedInterface) {
    Type genericInterface = null;
    while (genericInterface == null) {
      genericInterface = findGenericInterface(implementation, parametrizedInterface);
      if (implementation == Object.class && genericInterface == null) {
        throw new IllegalArgumentException("Class " + implementation.getSimpleName() + " does not implement " + parametrizedInterface.getSimpleName() + " interface.");
      }
      if (implementation != Object.class) {
        implementation = implementation.getSuperclass();
      }
    }
    if (genericInterface instanceof ParameterizedType) {
      return ((ParameterizedType) (genericInterface)).getActualTypeArguments();
    }
    throw new IllegalArgumentException("Interface " + parametrizedInterface.getSimpleName() + " is not parametrized");
  }

  private static Type findGenericInterface(Class implementation, Class parametrizedInterface) {
    for (int i = 0; i < implementation.getInterfaces().length; i++) {
      Class interfaceOfClass = implementation.getInterfaces()[i];
      if (parametrizedInterface.equals(interfaceOfClass)) {
        return implementation.getGenericInterfaces()[i];
      }
    }
    return null;
  }

  public static <T> T chooseBestClassStrategy(Map<Class, T> strategies, Class requiredType) {
    Class currentType = requiredType;
    T extractor = null;
    while (extractor == null) {
      extractor = strategies.get(currentType);
      if (extractor == null) {
        extractor = checkImplementedInterfaces(strategies, currentType);
      }
      if (extractor == null && currentType == Object.class) {
        throw new BristleInitializationException("Cannot retrieve strategy for type: " + requiredType);
      }
      currentType = currentType.getSuperclass();
      if (currentType == null) {
        currentType = Object.class;
      }
    }
    return extractor;
  }

  private static <T> T checkImplementedInterfaces(Map<Class, T> strategies, Class currentType) {
    Class[] interfaces = currentType.getInterfaces();
    for (Class interFace : interfaces) {
      if (strategies.containsKey(interFace)) {
        return strategies.get(interFace);
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public static <T> T findAnnotation(Annotation[] annotations, Class<T> annotationType) {
    for (Annotation annotation : annotations) {
      if (annotation.annotationType().equals(annotationType)) {
        return (T) annotation;
      }
    }
    return null;
  }
} 