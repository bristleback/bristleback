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

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains application contexts, provided both by the application and Bristleback framework.
 * Also it has equivalent of the String application context methods, but using both contexts.
 * <p/>
 * Created on: 2011-01-24 20:14:35 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class BristleSpringIntegration {
  private static Logger log = Logger.getLogger(BristleSpringIntegration.class.getName());

  private ApplicationContext actualContext;
  private ApplicationContext bristlebackFrameworkContext;

  public BristleSpringIntegration(ApplicationContext actualContext, ApplicationContext bristlebackFrameworkContext) {
    this.actualContext = actualContext;
    this.bristlebackFrameworkContext = bristlebackFrameworkContext;
  }

  public ApplicationContext getActualContext() {
    return actualContext;
  }

  public ApplicationContext getBristlebackFrameworkContext() {
    return bristlebackFrameworkContext;
  }

  public <T> T getFrameworkBean(String beanName, Class<T> beanClass) {
    return bristlebackFrameworkContext.getBean(beanName, beanClass);
  }

  public <T> T getApplicationBean(String beanName, Class<T> beanClass) {
    return actualContext.getBean(beanName, beanClass);
  }

  public <T> T getBean(String beanName, Class<T> beanClass) {
    if (actualContext.containsBean(beanName)) {
      return getApplicationBean(beanName, beanClass);
    }
    return getFrameworkBean(beanName, beanClass);
  }

  public <T> T getApplicationBean(Class<T> beanClass) {
    return actualContext.getBean(beanClass);
  }

  public <T> Map<String, T> getBeansOfType(Class<T> beanClass) {
    Map<String, T> allBeans = new HashMap<String, T>();

    Map<String, T> frameworkBeans = getFrameworkBeansOfType(beanClass);
    Map<String, T> applicationBeans = getApplicationBeansOfType(beanClass);

    allBeans.putAll(frameworkBeans);
    allBeans.putAll(applicationBeans);

    return allBeans;
  }

  public <T> Map<String, T> getFrameworkBeansOfType(Class<T> beanClass) {
    return bristlebackFrameworkContext.getBeansOfType(beanClass);
  }

  public <T> Map<String, T> getApplicationBeansOfType(Class<T> beanClass) {
    return actualContext.getBeansOfType(beanClass);
  }

  public boolean isSingleton(String beanName) {
    if (actualContext.containsBean(beanName)) {
      return actualContext.isSingleton(beanName);
    }
    return bristlebackFrameworkContext.isSingleton(beanName);
  }
}
