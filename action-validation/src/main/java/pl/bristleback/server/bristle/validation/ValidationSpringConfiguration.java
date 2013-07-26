package pl.bristleback.server.bristle.validation;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorMapping;

import javax.validation.Validation;

@Component
public class ValidationSpringConfiguration implements BeanFactoryPostProcessor {

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    ActionInterceptorMapping mapping = new ActionInterceptorMapping(ValidatorActionInterceptor.class, new ValidationInterceptorMatcher());
    beanFactory.registerSingleton("ActionInterceptorMapping", mapping);
    beanFactory.registerSingleton("validatorFactory", Validation.buildDefaultValidatorFactory());
  }
}
