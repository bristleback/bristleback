package pl.bristleback.server.bristle.serialization.system.json;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.common.serialization.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.system.BristleSerializationResolver;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;
import pl.bristleback.server.bristle.utils.PropertyUtils;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
@Ignore
public class JsonFastDeserializerPerformanceTest extends AbstractJUnit4SpringContextTests {

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private BristleSerializationResolver serializationResolver;

  private JsonFastDeserializer deserializer;

  private Long rawLong;

  @Serialize(format = "0.0000")
  private BigDecimal rawCustomFormatBigDecimal;

  private Double[] rawObjectArray;

  private List<VerySimpleMockBean> beanCollection;

  private Map<String, Long> rawMap;

  private Map<String, VerySimpleMockBean> beanMap;

  private BristleMessage<String> bristleMessage;

  @Before
  public void setUp() {
    deserializer = mockBeansFactory.getFrameworkBean(JsonFastDeserializer.class);
    serializationResolver = mockBeansFactory.getFrameworkBean("system.serializationResolver", BristleSerializationResolver.class);
  }

  @Test
  public void testRawSimpleValue() throws Exception {
    Locale.setDefault(Locale.ENGLISH);
    String serializedForm = "332221";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerPerformanceTest.class, "rawLong");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type, getFieldsAnnotations("rawLong"));

    measurePerformance(serializedForm, serialization, "Long raw value");
  }

  @Test
  public void deserializeBigDecimalCustomFormatValue() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    String serializedForm = "332.221";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerPerformanceTest.class, "rawCustomFormatBigDecimal");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type, getFieldsAnnotations("rawCustomFormatBigDecimal"));

    measurePerformance(serializedForm, serialization, "BigDecimal FORMATTED value");
  }

  @Test
  public void deserializeRawObjectArray() throws Exception {
    //given
    String serializedForm = "[1.0, 2, 3.1]";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerPerformanceTest.class, "rawObjectArray");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    measurePerformance(serializedForm, serialization, "Double[] array (3 elements)");
  }

  @Test
  public void deserializeBeanCollection() throws Exception {
    //given
    String serializedForm = "[{\"simpleField\":1},{\"simpleField\":2},{\"simpleField\":3}]";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerPerformanceTest.class, "beanCollection");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    measurePerformance(serializedForm, serialization, "List<VerySimpleMockBean> (3 elements)");
  }

  @Test
  public void deserializeRawMap() throws Exception {
    //given
    String serializedForm = "{\"a\":11, \"b\":22, \"c\":33}";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerPerformanceTest.class, "rawMap");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    measurePerformance(serializedForm, serialization, "Map<String, Long> (3 elements)");
  }

  @Test
  public void deserializeBeanMap() throws Exception {
    //given
    String serializedForm = "{\"a\":{\"simpleField\":11}, \"b\":{\"simpleField\":22}, \"c\":{\"simpleField\":33}}";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerPerformanceTest.class, "beanMap");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    measurePerformance(serializedForm, serialization, "Map<String, VerySimpleMockBean> (3 elements)");
  }

  @Test
  public void deserializeBristleMessage() throws Exception {
    //given
    String serializedForm = "{\"id\":12, \"name\":\"actionClass.action\", \"payload\":{\"simpleField\":22}}";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerPerformanceTest.class, "bristleMessage");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    measurePerformance(serializedForm, serialization, "BristleMessage");
  }

  private void measurePerformance(String serializedForm, PropertySerialization serialization, String typeMessage) throws Exception {
    //warming up
    performTests(serializedForm, serialization, 10000);

    // realTests
    int iterations = 10000000;
    long startTime = System.nanoTime();
    performTests(serializedForm, serialization, iterations);
    long endTime = System.nanoTime();

    printTotalTime(startTime, endTime, iterations, typeMessage);
  }

  private void performTests(String serializedForm, PropertySerialization serialization, int iterations) throws Exception {
    for (int i = 0; i < iterations; i++) {
      Object deserialized = deserializer.deserialize(serializedForm, serialization);
    }
  }

  private void printTotalTime(long startTime, long endTime, int iterations, String objectTypeMessage) {
    long totalTime = (endTime - startTime) / 1000000;
    System.out.println("Total time for test: " + objectTypeMessage + " \t for " + iterations + " iterations: " + totalTime + " milliseconds.");
  }

  private Annotation[] getFieldsAnnotations(String fieldName) throws NoSuchFieldException {
    return getClass().getDeclaredField(fieldName).getAnnotations();
  }
}
