package pl.bristleback.server.bristle.validation.init;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.action.interceptor.ActionInterceptorMapping;
import pl.bristleback.server.bristle.validation.ValidationInterceptorMatcher;
import pl.bristleback.server.bristle.validation.ValidatorActionInterceptor;

@Component
public class ValidationSpringConfiguration implements BeanFactoryPostProcessor {

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    ActionInterceptorMapping mapping = new ActionInterceptorMapping(ValidatorActionInterceptor.class, new ValidationInterceptorMatcher());
    beanFactory.registerSingleton("ActionInterceptorMapping", mapping);
  }
}
