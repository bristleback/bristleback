package pl.bristleback.server.bristle.conf.resolver.spring;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pl.bristleback.server.bristle.conf.resolver.message.ObjectSenderInjector;
import pl.bristleback.server.bristle.conf.resolver.message.RegisteredObjectSenders;
import pl.bristleback.server.bristle.message.ConditionObjectSender;

public class ObjectSenderBeanPostProcessor extends ObjectSenderInjector implements BeanPostProcessor, ApplicationContextAware {

  private ApplicationContext applicationContext;

  public ObjectSenderBeanPostProcessor(RegisteredObjectSenders registeredObjectSenders) {
    super(registeredObjectSenders);
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    injectSenders(bean);
    return bean;
  }

  @Override
  protected ConditionObjectSender getObjectSenderInstance() {
    return applicationContext.getBean(ConditionObjectSender.class);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

}
