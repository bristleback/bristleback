package pl.bristleback.server.bristle.api;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface ApplicationComponentsContainer {

  <T> T getBean(Class<T> beanClass);

  <T> T getBean(String beanName, Class<T> beanClass);

  boolean containsBean(String beanName);

  <T> Map<String,T> getBeansOfType(Class<T> beanClass);

  boolean isSingleton(String beanName);

  Map<String,Object> getBeansWithAnnotation(Class<? extends Annotation> annotation);
}
