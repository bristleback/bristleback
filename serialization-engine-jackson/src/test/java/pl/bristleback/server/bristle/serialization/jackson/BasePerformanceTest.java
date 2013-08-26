/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

package pl.bristleback.server.bristle.serialization.jackson;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.api.SerializationEngine;
import pl.bristleback.server.bristle.api.SerializationResolver;
import pl.bristleback.server.bristle.utils.PropertyUtils;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public abstract class BasePerformanceTest extends AbstractJUnit4SpringContextTests {

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  protected SerializationResolver serializationResolver;

  protected SerializationEngine serializer;

  protected Long rawLong;

  private BigDecimal rawCustomFormatBigDecimal;

  private Double[] rawObjectArray;

  private List<VerySimpleMockBean> beanCollection;

  private Map<String, Long> rawMap;

  private Map<String, VerySimpleMockBean> beanMap;

  private BristleMessage<String> bristleMessage;

  @Before
  public void setUp() {
    serializer = mockBeansFactory.getFrameworkBean("system.serializer.jackson", JacksonSerializationEngine.class);
    serializationResolver = mockBeansFactory.getFrameworkBean("jacksonSerializer.serializationResolver", JacksonSerializationResolver.class);
  }

  protected Object getSerialization(String fieldName) throws NoSuchFieldException {
    Type type = PropertyUtils.getDeclaredFieldType(BasePerformanceTest.class, fieldName);
    return serializationResolver.resolveSerialization(type, getFieldsAnnotations(fieldName));
  }

  protected void measurePerformance(Object serializationObject, Object serialization, String typeMessage) throws Exception {
    //warming up
    performTests(serializationObject, serialization, 10000);

    // realTests
    int iterations = 10000000;
    long startTime = System.nanoTime();
    performTests(serializationObject, serialization, iterations);
    long endTime = System.nanoTime();

    printTotalTime(startTime, endTime, iterations, typeMessage);
  }

  @SuppressWarnings("unchecked")
  abstract protected void performTests(Object serializationObject, Object serialization, int iterations) throws Exception;

  private void printTotalTime(long startTime, long endTime, int iterations, String objectTypeMessage) {
    long totalTime = (endTime - startTime) / 1000000;
    System.out.println("Total time for test: " + objectTypeMessage + " \t for " + iterations + " iterations: " + totalTime + " milliseconds.");
  }

  protected Annotation[] getFieldsAnnotations(String fieldName) throws NoSuchFieldException {
    return getClass().getSuperclass().getDeclaredField(fieldName).getAnnotations();
  }
}
