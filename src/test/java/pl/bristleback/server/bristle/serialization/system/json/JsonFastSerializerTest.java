package pl.bristleback.server.bristle.serialization.system.json;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.serialization.system.BristleSerializationResolver;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.utils.PropertyUtils;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-10-08 16:27:17 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class JsonFastSerializerTest extends AbstractJUnit4SpringContextTests {
  private static Logger log = Logger.getLogger(JsonFastSerializerTest.class.getName());

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private JsonFastSerializer fastSerializer;
  private BristleSerializationResolver serializationResolver;

  private double[] rawArray;
  private VerySimpleMockBean[] beanArray;

  private List<Integer> sampleList;
  private List<VerySimpleMockBean> beanList;

  private Map<String, BigDecimal> rawMap;

  @Before
  public void setUp() {
    fastSerializer = mockBeansFactory.getFrameworkBean("jsonSerializer.fastSerializer", JsonFastSerializer.class);
    serializationResolver = mockBeansFactory.getFrameworkBean("system.serializationResolver", BristleSerializationResolver.class);
  }

  @Test
  public void serializeRawValue() throws Exception {
    Integer four = 4;
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(four.getClass());
    String result = fastSerializer.serializeObject(four, serialization);
    assertEquals(four + "", result);
  }

  @Test
  public void serializeRawArray() throws Exception {
    int size = 4;
    rawArray = new double[size];
    for (int i = 1; i <= size; i++) {
      rawArray[i - 1] = (double) i;
    }
    Type listType = PropertyUtils.getDeclaredField(JsonFastSerializerTest.class, "rawArray");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(listType);
    String result = fastSerializer.serializeObject(rawArray, serialization);
    String expectedResult = "[1.0,2.0,3.0,4.0]";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeBeanArray() throws Exception {
    int size = 2;
    beanArray = new VerySimpleMockBean[size];
    for (int i = 1; i <= size; i++) {
      VerySimpleMockBean beanToAdd = new VerySimpleMockBean();
      beanToAdd.setSimpleField(i);
      beanArray[i - 1] = beanToAdd;
    }
    Type listType = PropertyUtils.getDeclaredField(JsonFastSerializerTest.class, "beanArray");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(listType);
    String result = fastSerializer.serializeObject(beanArray, serialization);
    String expectedResult = "[{\"simpleField\":1},{\"simpleField\":2}]";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawList() throws Exception {
    int size = 4;
    sampleList = new ArrayList<Integer>();
    for (int i = 1; i <= size; i++) {
      sampleList.add(i);
    }
    Type listType = PropertyUtils.getDeclaredField(JsonFastSerializerTest.class, "sampleList");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(listType);
    String result = fastSerializer.serializeObject(sampleList, serialization);
    String expectedResult = "[1,2,3,4]";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeBeanList() throws Exception {
    int size = 2;
    beanList = new ArrayList<VerySimpleMockBean>();
    for (int i = 1; i <= size; i++) {
      VerySimpleMockBean beanToAdd = new VerySimpleMockBean();
      beanToAdd.setSimpleField(i);
      beanList.add(beanToAdd);
    }
    Type listType = PropertyUtils.getDeclaredField(JsonFastSerializerTest.class, "beanList");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(listType);
    String result = fastSerializer.serializeObject(beanList, serialization);
    String expectedResult = "[{\"simpleField\":1},{\"simpleField\":2}]";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawMap() throws Exception {
    int size = 2;
    rawMap = new LinkedHashMap<String, BigDecimal>();
    for (int i = 1; i <= size; i++) {
      rawMap.put(i + "s", new BigDecimal(i));
    }
    Type listType = PropertyUtils.getDeclaredField(JsonFastSerializerTest.class, "rawMap");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(listType);
    String result = fastSerializer.serializeObject(rawMap, serialization);
    String expectedResult = "{\"1s\":1,\"2s\":2}";
    assertEquals(expectedResult, result);
  }

} 