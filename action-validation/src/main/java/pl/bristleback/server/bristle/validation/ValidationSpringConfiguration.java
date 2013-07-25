package pl.bristleback.server.bristle.validation;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorMapping;

import javax.inject.Inject;

@Component
public class ValidationSpringConfiguration implements BeanFactoryPostProcessor {

  @Inject
  private ValidationInterceptorMatcher validationInterceptorMatcher;

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    ActionInterceptorMapping mapping = new ActionInterceptorMapping(ValidatorActionInterceptor.class, validationInterceptorMatcher);
    beanFactory.registerSingleton("ActionInterceptorMapping", mapping);
  }
}
