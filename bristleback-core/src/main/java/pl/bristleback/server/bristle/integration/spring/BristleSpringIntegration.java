/*
 * // Bristleback plugin - Copyright (c) 2010 bristleback.googlecode.com
 * // ---------------------------------------------------------------------------
 * // This program is free software; you can redistribute it and/or modify it
 * // under the terms of the GNU Lesser General Public License as published by the
 * // Free Software Foundation; either version 3 of the License, or (at your
 * // option) any later version.
 * // This library is distributed in the hope that it will be useful,
 * // but without any warranty; without even the implied warranty of merchantability
 * // or fitness for a particular purpose.
 * // You should have received a copy of the GNU Lesser General Public License along
 * // with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * // ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.integration.spring;

import org.springframework.context.ApplicationContext;
import pl.bristleback.server.bristle.BristleRuntimeException;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains application contexts, provided both by the application and Bristleback framework.
 * Also it has equivalent of the Spring application context methods, but using both contexts.
 * All the rules applicable in the Spring application context methods are applicable in this class.
 * <p/>
 * Created on: 2011-01-24 20:14:35 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class BristleSpringIntegration {

  private ApplicationContext actualContext;

  private ApplicationContext bristlebackFrameworkContext;

  /**
   * Creates the new Spring integration object, containing both application and Bristleback Server Spring contexts.
   *
   * @param actualContext               actual application context.
   * @param bristlebackFrameworkContext Bristleback Server internal context.
   */
  public BristleSpringIntegration(ApplicationContext actualContext, ApplicationContext bristlebackFrameworkContext) {
    this.actualContext = actualContext;
    this.bristlebackFrameworkContext = bristlebackFrameworkContext;
  }

  /**
   * Gets the actual application context.
   *
   * @return actual application context.
   */
  public ApplicationContext getActualContext() {
    return actualContext;
  }

  /**
   * Gets the Bristleback internal Server Spring context.
   *
   * @return Bristleback internal Server Spring context.
   */
  public ApplicationContext getBristlebackFrameworkContext() {
    return bristlebackFrameworkContext;
  }

  /**
   * Gets the Bristleback internal Spring bean instance with class and name provided as parameters.
   *
   * @param beanName  name of the Spring bean.
   * @param beanClass class of the Spring bean.
   * @param <T>       type of bean.
   * @return Bristleback internal Spring bean instance.
   */
  public <T> T getFrameworkBean(String beanName, Class<T> beanClass) {
    return bristlebackFrameworkContext.getBean(beanName, beanClass);
  }

  /**
   * Gets the actual application context Spring bean instance with class and name provided as parameters.
   *
   * @param beanName  name of the Spring bean.
   * @param beanClass class of the Spring bean.
   * @param <T>       type of bean.
   * @return actual application context Spring bean instance.
   */
  public <T> T getApplicationBean(String beanName, Class<T> beanClass) {
    return actualContext.getBean(beanName, beanClass);
  }

  /**
   * Gets the Spring bean instance with class and name provided as parameters,
   * both actual application context and Bristleback internal context are searched.
   *
   * @param beanName  name of the Spring bean.
   * @param beanClass class of the Spring bean.
   * @param <T>       type of bean.
   * @return Spring bean instance either from the actual application context or Bristleback internal context.
   */
  public <T> T getBean(String beanName, Class<T> beanClass) {
    if (actualContext.containsBean(beanName)) {
      return getApplicationBean(beanName, beanClass);
    }
    return getFrameworkBean(beanName, beanClass);
  }

  public <T> T getBean(Class<T> beanClass) {
    Map<String, T> beansOfType = actualContext.getBeansOfType(beanClass);
    if (beansOfType.isEmpty()) {
      return bristlebackFrameworkContext.getBean(beanClass);
    }
    if (beansOfType.size() > 1) {
      throw new BristleRuntimeException("More than one matching Spring bean found for type: " + beanClass);
    }
    return actualContext.getBean(beanClass);
  }

  /**
   * Gets the actual application bean instance that uniquely matches the given object type, if any.
   *
   * @param beanClass class of the Spring bean.
   * @param <T>       type of bean.
   * @return actual application bean instance that uniquely matches the given object type, if any.
   */
  public <T> T getApplicationBean(Class<T> beanClass) {
    return actualContext.getBean(beanClass);
  }

  /**
   * Return the bean instances that match the given object type (including subclasses)
   * taken from both actual and Bristleback internal application contexts,
   * judging from either bean definitions or the value of getObjectType in the case of FactoryBeans.
   *
   * @param beanClass the class or interface to match, or null for all concrete beans
   * @param <T>       type of bean.
   * @return a Map with the matching beans, containing the bean names as keys and the corresponding bean instances as values.
   */
  public <T> Map<String, T> getBeansOfType(Class<T> beanClass) {
    Map<String, T> allBeans = new HashMap<String, T>();

    Map<String, T> frameworkBeans = getFrameworkBeansOfType(beanClass);
    Map<String, T> applicationBeans = getApplicationBeansOfType(beanClass);

    allBeans.putAll(frameworkBeans);
    allBeans.putAll(applicationBeans);

    return allBeans;
  }

  /**
   * Return the bean instances that match the given object type (including subclasses)
   * taken from Bristleback internal application context,
   * judging from either bean definitions or the value of getObjectType in the case of FactoryBeans.
   *
   * @param beanClass the class or interface to match, or null for all concrete beans
   * @param <T>       type of bean.
   * @return a Map with the matching beans, containing the bean names as keys and the corresponding bean instances as values.
   */
  public <T> Map<String, T> getFrameworkBeansOfType(Class<T> beanClass) {
    return bristlebackFrameworkContext.getBeansOfType(beanClass);
  }

  /**
   * Return the bean instances that match the given object type (including subclasses) taken from actual application context,
   * judging from either bean definitions or the value of getObjectType in the case of FactoryBeans.
   *
   * @param beanClass the class or interface to match, or null for all concrete beans.
   * @param <T>       type of bean
   * @return a Map with the matching beans, containing the bean names as keys and the corresponding bean instances as values.
   */
  public <T> Map<String, T> getApplicationBeansOfType(Class<T> beanClass) {
    return actualContext.getBeansOfType(beanClass);
  }

  /**
   * Checks whether this bean corresponds to a singleton instance. Both actual and Bristleback context are searched.
   *
   * @param beanName name of the Spring bean.
   * @return whether this bean corresponds to a singleton instance.
   */
  public boolean isSingleton(String beanName) {
    if (actualContext.containsBean(beanName)) {
      return actualContext.isSingleton(beanName);
    }
    return bristlebackFrameworkContext.isSingleton(beanName);
  }

  public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
    Map<String, Object> beans = actualContext.getBeansWithAnnotation(annotation);
    beans.putAll(bristlebackFrameworkContext.getBeansWithAnnotation(annotation));
    return beans;
  }
}
