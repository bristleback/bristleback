package pl.bristleback.server.bristle.conf.resolver.plain;


import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.TruePredicate;
import pl.bristleback.server.bristle.api.ApplicationComponentsResolver;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlainJavaApplicationComponentsResolver implements ApplicationComponentsResolver {

  private Map<String, Object> components = new HashMap<String, Object>();

  public void addComponent(String name, Object component) {
    components.put(name, new JavaComponentInformation(component));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getBean(final Class<T> beanClass) {
    Collection<T> beansFound = getBeansOfType(beanClass).values();
    if (beansFound.isEmpty() || beansFound.size() > 0) {
      throw new BristleInitializationException("Could not find exactly one application component of type " + beanClass
        + ", components found: " + beansFound.size());
    }
    return (T) beansFound.iterator().next();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getBean(String beanName, Class<T> beanClass) {
    Object componentInformation = components.get(beanName);
    if (componentInformation == null) {
      throw new BristleInitializationException("Could not find application component with name \"" + beanName + "\"");
    }
    if (!beanClass.isAssignableFrom(componentInformation.getClass())) {
      throw new BristleInitializationException("Component with name " + beanName + " is not of type: " + beanClass
        + ", actual type: " + componentInformation.getClass());
    }
    return (T) componentInformation;
  }

  @Override
  public boolean containsBean(String beanName) {
    return components.containsKey(beanName);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Map<String, T> getBeansOfType(final Class<T> beanClass) {
    return MapUtils.predicatedMap(components, TruePredicate.getInstance(), new Predicate() {
      @Override
      public boolean evaluate(Object object) {
        return beanClass.isAssignableFrom(object.getClass());
      }
    });
  }

  @Override
  public boolean isSingleton(String beanName) {
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> getBeansWithAnnotation(final Class<? extends Annotation> annotationType) {
    return MapUtils.predicatedMap(components, TruePredicate.getInstance(), new Predicate() {
      @Override
      public boolean evaluate(Object object) {
        Annotation annotation = object.getClass().getAnnotation(annotationType);
        return annotation != null;
      }
    });
  }
}
