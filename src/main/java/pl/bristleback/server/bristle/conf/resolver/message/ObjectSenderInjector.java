package pl.bristleback.server.bristle.conf.resolver.message;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pl.bristleback.server.bristle.api.annotations.AnnotatedObjectSender;
import pl.bristleback.server.bristle.message.sender.ConditionObjectSender;
import pl.bristleback.server.bristle.serialization.SerializationBundle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-06-11 13:45:23 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ObjectSenderInjector implements BeanPostProcessor, ApplicationContextAware {
  private static Logger log = Logger.getLogger(ObjectSenderInjector.class.getName());

  private ApplicationContext applicationContext;

  private List<ConditionObjectSender> registeredSenders = new ArrayList<ConditionObjectSender>();

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    Field[] fields = bean.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(AnnotatedObjectSender.class)) {
        ConditionObjectSender objectSender = resolveSender(field);
        injectProperty(bean, field, objectSender);
      }
    }
    return bean;
  }

  private void injectProperty(Object bean, Field field, ConditionObjectSender objectSender) {
    try {
      FieldUtils.writeDeclaredField(bean, field.getName(), objectSender, true);
    } catch (IllegalAccessException e) {
      throw new BeanInstantiationException(bean.getClass(), e.getMessage(), e);
    }
  }

  private ConditionObjectSender resolveSender(Field field) {
    ConditionObjectSender conditionObjectSender = applicationContext.getBean(ConditionObjectSender.class);
    SerializationBundle serializationBundle = new SerializationBundle(field);

    conditionObjectSender.setSerializationBundle(serializationBundle);
    registeredSenders.add(conditionObjectSender);

    return conditionObjectSender;
  }

  public List<ConditionObjectSender> getRegisteredSenders() {
    return registeredSenders;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

}

