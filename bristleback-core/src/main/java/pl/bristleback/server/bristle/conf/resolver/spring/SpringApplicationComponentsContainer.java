package pl.bristleback.server.bristle.conf.resolver.spring;


import org.springframework.context.ApplicationContext;
import pl.bristleback.server.bristle.api.ApplicationComponentsContainer;

import java.lang.annotation.Annotation;
import java.util.Map;

public class SpringApplicationComponentsContainer implements ApplicationComponentsContainer {

  private ApplicationContext applicationContext;

  public SpringApplicationComponentsContainer(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public <T> T getBean(Class<T> beanClass) {
    return applicationContext.getBean(beanClass);
  }

  @Override
  public <T> T getBean(String beanName, Class<T> beanClass) {
    return applicationContext.getBean(beanName, beanClass);
  }

  @Override
  public boolean containsBean(String beanName) {
    return applicationContext.containsBean(beanName);
  }

  @Override
  public <T> Map<String, T> getBeansOfType(Class<T> beanClass) {
    return applicationContext.getBeansOfType(beanClass);
  }

  @Override
  public boolean isSingleton(String beanName) {
    return applicationContext.isSingleton(beanName);
  }

  @Override
  public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
    return applicationContext.getBeansWithAnnotation(annotation);
  }
}
