/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.utils;

import pl.bristleback.server.bristle.conf.BristleInitializationException;
import pl.bristleback.server.bristle.serialization.SerializationResolvingException;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * This class contains utility methods related with Java reflection mechanism.
 * <p/>
 * Created on: 2011-07-22 10:17:24 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class ReflectionUtils {

  public static final int COLLECTION_ELEMENT_PARAMETER_INDEX = 0;

  public static final int MAP_ELEMENT_PARAMETER_INDEX = 1;

  private static final Map<Class, Class> PRIMITIVE_TO_WRAPPERS_MAP = new HashMap<Class, Class>();

  private static final Map<Class, Class> WRAPPERS_TO_PRIMITIVE_MAP = new HashMap<Class, Class>();

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

  public static Class getWrapperClassForPrimitive(Class primitiveClass) {
    return PRIMITIVE_TO_WRAPPERS_MAP.get(primitiveClass);
  }

  public static Class getPrimitiveForWrapper(Class wrapperClass) {
    return WRAPPERS_TO_PRIMITIVE_MAP.get(wrapperClass);
  }

  public static Type[] getParameterTypes(Class implementation, Class parametrizedInterface) {
    Type genericInterface = null;
    List<Class> classesStack = new LinkedList<Class>();
    classesStack.add(implementation);
    while (genericInterface == null) {
      genericInterface = findGenericInterface(implementation, parametrizedInterface);
      if (implementation == Object.class && genericInterface == null) {
        throw new IllegalArgumentException("Class " + implementation.getSimpleName() + " does not implement " + parametrizedInterface.getSimpleName() + " interface.");
      }
      if (implementation != Object.class) {
        implementation = implementation.getSuperclass();
        classesStack.add(implementation);
      }
    }
    if (genericInterface instanceof ParameterizedType) {
      return findRealTypeArguments(genericInterface, classesStack);
    }
    throw new IllegalArgumentException("Interface " + parametrizedInterface.getSimpleName() + " is not parametrized");
  }

  private static Type[] findRealTypeArguments(Type genericInterface, List<Class> classesStack) {
    Type[] interfaceTypeArguments = ((ParameterizedType) (genericInterface)).getActualTypeArguments();
    Type[] realTypeArguments = new Type[interfaceTypeArguments.length];
    for (int i = 0; i < interfaceTypeArguments.length; i++) {
      Type interfaceTypeArgument = interfaceTypeArguments[i];
      if (interfaceTypeArgument instanceof TypeVariable) {
        realTypeArguments[i] = extractRealTypeFromTypeVariable(classesStack, (TypeVariable) interfaceTypeArgument);
      } else {
        realTypeArguments[i] = interfaceTypeArgument;
      }
    }
    return realTypeArguments;
  }

  private static Type extractRealTypeFromTypeVariable(List<Class> classesStack, TypeVariable interfaceTypeArgument) {
    String name = interfaceTypeArgument.getName();
    for (ListIterator<Class> iterator = classesStack.listIterator(classesStack.size()); iterator.hasPrevious();) {
      Class clazz = iterator.previous();
      Type realType = extractRealTypeFromClass(clazz, iterator, name);
      if (realType != null) {
        return realType;
      }
    }
    throw new SerializationResolvingException("Could not extract real type from type variable");
  }

  private static Type extractRealTypeFromClass(Class clazz, ListIterator<Class> iterator, String name) {
    for (int j = 0; j < clazz.getTypeParameters().length; j++) {
      TypeVariable typeVariableFromClass = clazz.getTypeParameters()[j];
      if (name.equals(typeVariableFromClass.getName())) {
        return findClassParameterRealType(iterator, j);
      }
    }
    return null;
  }

  private static Type findClassParameterRealType(ListIterator<Class> iterator, int index) {
    Class childClass = iterator.previous();
    ParameterizedType genericSuperClassType = (ParameterizedType) childClass.getGenericSuperclass();
    Type type = genericSuperClassType.getActualTypeArguments()[index];
    if (type instanceof TypeVariable) {
      return extractRealTypeFromClass(childClass, iterator, ((TypeVariable) type).getName());
    }
    return type;
  }

  private static Type findGenericInterface(Class<?> implementation, Class<?> parametrizedInterface) {
    for (int i = 0; i < implementation.getInterfaces().length; i++) {
      Class<?> interfaceOfClass = implementation.getInterfaces()[i];
      if (parametrizedInterface.isAssignableFrom(interfaceOfClass)) {
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