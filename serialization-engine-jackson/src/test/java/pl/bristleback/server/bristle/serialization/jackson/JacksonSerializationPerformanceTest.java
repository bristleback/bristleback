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


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
@Ignore
public class JacksonSerializationPerformanceTest extends BasePerformanceTest {


  @Test
  public void serializeRawSimpleValue() throws Exception {
    Locale.setDefault(Locale.ENGLISH);
    long deserializedForm = 332221L;
    Object serialization = getSerialization("rawLong");

    measurePerformance(deserializedForm, serialization, "Long raw value");
  }

  @Test
  public void serializeRawObjectArray() throws Exception {
    //given
    Double[] objectForm = new Double[]{1.0, 2.0, 3.1};
    Object serialization = getSerialization("rawObjectArray");

    measurePerformance(objectForm, serialization, "Double[] array (3 elements)");
  }

  @Test
  public void serializeBeanCollection() throws Exception {
    //given
    List<VerySimpleMockBean> list = Arrays.asList(new VerySimpleMockBean(1), new VerySimpleMockBean(2), new VerySimpleMockBean(3));
    Object serialization = getSerialization("beanCollection");

    measurePerformance(list, serialization, "List<VerySimpleMockBean> (3 elements)");
  }

  @Test
  public void serializeRawMap() throws Exception {
    //given
    Map<String, Long> map = new HashMap<String, Long>();
    map.put("a", 11L);
    map.put("b", 22L);
    map.put("c", 33L);
    Object serialization = getSerialization("rawMap");

    measurePerformance(map, serialization, "Map<String, Long> (3 elements)");
  }

  @Test
  public void serializeBeanMap() throws Exception {
    //given
    Map<String, VerySimpleMockBean> map = new HashMap<String, VerySimpleMockBean>();
    map.put("a", new VerySimpleMockBean(11));
    map.put("b", new VerySimpleMockBean(22));
    map.put("c", new VerySimpleMockBean(33));
    Object serialization = getSerialization("beanMap");

    measurePerformance(map, serialization, "Map<String, VerySimpleMockBean> (3 elements)");
  }

  @Test
  public void serializeBristleMessage() throws Exception {
    //given
    BristleMessage<String> bristleMessage = new BristleMessage<String>()
      .withId("12")
      .withName("actionClass.action")
      .withPayload("payload");
    Object serialization = getSerialization("bristleMessage");

    measurePerformance(bristleMessage, serialization, "BristleMessage");
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void performTests(Object serializationObject, Object serialization, int iterations) throws Exception {
    for (int i = 0; i < iterations; i++) {
      String serialized = serializer.serialize(serializationObject, serialization);
    }
  }
}

