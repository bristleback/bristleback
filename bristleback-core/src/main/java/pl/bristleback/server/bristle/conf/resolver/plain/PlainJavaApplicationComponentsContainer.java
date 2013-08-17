package pl.bristleback.server.bristle.conf.resolver.plain;


import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import pl.bristleback.server.bristle.api.ApplicationComponentsContainer;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlainJavaApplicationComponentsContainer implements ApplicationComponentsContainer {

  private Map<String, Object> components = new HashMap<String, Object>();

  public void addComponent(String name, Object component) {
    components.put(name, component);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getBean(final Class<T> beanClass) {
    Collection<T> beansFound = getBeansOfType(beanClass).values();
    if (beansFound.isEmpty() || beansFound.size() > 1) {
      throw new NoSuchBeanDefinitionException("Could not find exactly one application component of type " + beanClass
        + ", components found: " + beansFound.size());
    }
    return beansFound.iterator().next();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getBean(String beanName, Class<T> beanClass) {
    Object componentInformation = components.get(beanName);
    if (componentInformation == null) {
      throw new BristleInitializationException("Could not find application component with name \"" + beanName + "\"");
    }
    if (!beanClass.isAssignableFrom(componentInformation.getClass())) {
      throw new NoSuchBeanDefinitionException("Component with name " + beanName + " is not of type: " + beanClass
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
    return (Map<String, T>) FilteredMap.filteredValues(components, new TypedPredicate<Object>() {
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
    return FilteredMap.filteredValues(components, new TypedPredicate<Object>() {
      @Override
      public boolean evaluate(Object object) {
        Annotation annotation = object.getClass().getAnnotation(annotationType);
        return annotation != null;
      }
    });
  }
}
