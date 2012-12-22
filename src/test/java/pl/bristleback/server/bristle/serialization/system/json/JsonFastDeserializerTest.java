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
import pl.bristleback.server.bristle.serialization.system.SerializationException;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.bristle.serialization.system.annotation.Property;
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;
import pl.bristleback.server.bristle.utils.PropertyUtils;
import pl.bristleback.server.mock.beans.MockBean;
import pl.bristleback.server.mock.beans.SimpleMockBean;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-15 20:14:54 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class JsonFastDeserializerTest extends AbstractJUnit4SpringContextTests {
  private static Logger log = Logger.getLogger(JsonFastDeserializerTest.class.getName());

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private JsonFastDeserializer deserializer;
  private BristleSerializationResolver serializationResolver;


  private Long rawPropertyLong;

  @Bind(format = "yyyy-MM-dd hh:mm:ss")
  private Date rawCustomFormatDate;

  @Serialize(format = "0.0000")
  private BigDecimal rawCustomFormatBigDecimal;

  private double[] rawArray;
  private Double[] rawObjectArray;
  private VerySimpleMockBean[] beanArray;

  private List<Character> rawCollectionChar;
  private List<VerySimpleMockBean> beanCollectionChar;

  @Bind(properties = @Property(name = "simpleField", required = true))
  private List<VerySimpleMockBean> beanCollectionCharRequired;

  @Bind(properties = @Property(name = "arrayOfBeans.property1", required = true))
  private List<MockBean> notSimpleBeanCollectionFieldRequired;

  private Map<String, Long> rawMap;

  private Map<String, VerySimpleMockBean> beanMap;

  @Before
  public void setUp() {
    deserializer = mockBeansFactory.getFrameworkBean("jsonSerializer.fastDeserializer", JsonFastDeserializer.class);
    serializationResolver = mockBeansFactory.getFrameworkBean("system.serializationResolver", BristleSerializationResolver.class);
  }

  @Test
  public void deserializeRawCorrect() throws Exception {
    //given
    String serializedForm = "332";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawPropertyLong");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertEquals(332L, ((Long) deserialized).longValue());
  }

  @Test(expected = NumberFormatException.class)
  public void deserializeRawFormatException() throws Exception {
    //given
    String serializedForm = "33s2";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawPropertyLong");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertEquals(332L, ((Long) deserialized).longValue());
  }

  @Test
  public void deserializeRawEmptyValue() throws Exception {
    //given
    String serializedForm = "";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawPropertyLong");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertNull(deserialized);
  }

  @Test
  public void deserializeRawNullValue() throws Exception {
    //given
    String serializedForm = null;
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawPropertyLong");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertNull(deserialized);
  }

  @Test
  public void deserializeDateCustomFormatValue() throws Exception {
    //given
    String serializedForm = "1987-08-13 12:38:55";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawCustomFormatDate");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type, getFieldsAnnotations("rawCustomFormatDate"));

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date expectedDate = dateFormat.parse(serializedForm);
    assertEquals(expectedDate, deserialized);
  }

  @Test
  public void deserializeBigDecimalCustomFormatValue() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    String serializedForm = "332.221";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawCustomFormatBigDecimal");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type, getFieldsAnnotations("rawCustomFormatBigDecimal"));

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertEquals(new BigDecimal(serializedForm), deserialized);
  }

  @Test
  public void deserializeRawArray() throws Exception {
    //given
    String serializedForm = "[1.0, 2, 3.1]";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawArray");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertTrue(deserialized instanceof double[]);
    double[] deserializedArray = (double[]) deserialized;
    assertEquals(1.0, deserializedArray[0]);
  }

  @Test
  public void deserializeRawObjectArray() throws Exception {
    //given
    String serializedForm = "[1.0, 2, 3.1]";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawObjectArray");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertTrue(deserialized instanceof Double[]);
    Double[] deserializedArray = (Double[]) deserialized;
    assertEquals(1.0, deserializedArray[0]);
  }

  @Test
  public void deserializeRawCollection() throws Exception {
    //given
    String serializedForm = "[\"a\", \"b\", \"c\"]";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawCollectionChar");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertTrue(deserialized instanceof List);
    List deserializedList = (List) deserialized;
    assertEquals('a', deserializedList.get(0));
  }

  @Test
  public void deserializeBeanCollection() throws Exception {
    //given
    String serializedForm = "[{\"simpleField\":1},{\"simpleField\":2}]";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "beanCollectionChar");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertTrue(deserialized instanceof List);
    List deserializedList = (List) deserialized;
    VerySimpleMockBean firstElement = (VerySimpleMockBean) deserializedList.get(0);
    assertEquals(1, firstElement.getSimpleField());
  }

  @Test
  public void deserializeBeanCollectionOneEmpty() throws Exception {
    //given
    String serializedForm = "[{},{\"simpleField\":2}]";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "beanCollectionChar");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertTrue(deserialized instanceof List);
    List deserializedList = (List) deserialized;
    VerySimpleMockBean firstElement = (VerySimpleMockBean) deserializedList.get(0);
    assertEquals(0, firstElement.getSimpleField());
  }

  @Test
  public void deserializeBeanCollectionOneEmptyRequired() throws Exception {
    //given
    String serializedForm = "[{},{\"simpleField\":2}]";

    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "beanCollectionCharRequired");
    PropertySerialization serialization = serializationResolver
      .resolveSerialization(type, getFieldsAnnotations("beanCollectionCharRequired"));

    try {
      //when
      deserializer.deserialize(serializedForm, serialization);

      //then
      fail("Method should throw SerializationException");
    } catch (SerializationException e) {
      PropertySerialization elementSerialization = serialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      assertEquals(elementSerialization.getPropertySerialization("simpleField"), e.getInformation());
      assertEquals(SerializationException.Reason.NOT_NULL_VIOLATION, e.getReason());
    }
  }

  @Test
  public void deserializeNotSimpleBeanCollectionOneEmptyRequired() throws Exception {
    //given
    String serializedForm = "[{\"arrayOfBeans\": [{}, {}]}]";

    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "notSimpleBeanCollectionFieldRequired");
    PropertySerialization serialization = serializationResolver
      .resolveSerialization(type, getFieldsAnnotations("notSimpleBeanCollectionFieldRequired"));

    try {
      //when
      deserializer.deserialize(serializedForm, serialization);
      //then
      fail("Method should throw SerializationException");
    } catch (SerializationException e) {
      PropertySerialization elementSerialization = serialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      PropertySerialization nestedArraySerialization = elementSerialization.getPropertySerialization("arrayOfBeans");
      PropertySerialization nestedBeanSerialization = nestedArraySerialization
        .getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
      assertEquals(nestedBeanSerialization.getPropertySerialization("property1"), e.getInformation());
      assertEquals(SerializationException.Reason.NOT_NULL_VIOLATION, e.getReason());
    }
  }

  @Test
  public void deserializeNotSimpleBeanCollectionOneEmptyRequiredCorrect() throws Exception {
    //given
    String serializedForm = "[{\"arrayOfBeans\": [{\"property1\":1, \"property2\":\"string1\"}, {\"property1\":2, \"property2\":\"string2\"}]}]";

    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "notSimpleBeanCollectionFieldRequired");
    PropertySerialization serialization = serializationResolver
      .resolveSerialization(type, getFieldsAnnotations("notSimpleBeanCollectionFieldRequired"));

    //when
    List<MockBean> beans = (List<MockBean>) deserializer.deserialize(serializedForm, serialization);
    //then
    MockBean bean = beans.get(0);
    SimpleMockBean[] arrayOfBeans = bean.getArrayOfBeans();
    assertNotNull(arrayOfBeans);
    assertEquals(2, arrayOfBeans.length);
    SimpleMockBean firstBean = arrayOfBeans[0];
    assertEquals(1, firstBean.getProperty1());
    assertEquals("string2", arrayOfBeans[1].getProperty2());
  }

  @Test
  public void deserializeRawMap() throws Exception {
    //given
    String serializedForm = "{\"a\":11, \"b\":22, \"c\":33}";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "rawMap");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertTrue(deserialized instanceof Map);
    Map deserializedList = (Map) deserialized;
    assertEquals(33L, deserializedList.get("c"));
  }

  @Test
  public void deserializeBeanMap() throws Exception {
    //given
    String serializedForm = "{\"a\":{\"simpleField\":11}, \"b\":{\"simpleField\":22}, \"c\":{\"simpleField\":33}}";
    Type type = PropertyUtils.getDeclaredFieldType(JsonFastDeserializerTest.class, "beanMap");
    PropertySerialization serialization = serializationResolver.resolveSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertTrue(deserialized instanceof Map);
    Map<String, VerySimpleMockBean> deserializedList = (Map<String, VerySimpleMockBean>) deserialized;
    VerySimpleMockBean secondElement = deserializedList.get("b");
    assertEquals(22, secondElement.getSimpleField());
  }

  private Annotation[] getFieldsAnnotations(String fieldName) throws NoSuchFieldException {
    return getClass().getDeclaredField(fieldName).getAnnotations();
  }
}
