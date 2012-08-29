package pl.bristleback.server.bristle.serialization.system.json;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.api.annotations.Bind;
import pl.bristleback.server.bristle.api.annotations.Property;
import pl.bristleback.server.bristle.conf.resolver.serialization.SerializationInputResolver;
import pl.bristleback.server.bristle.serialization.SerializationInput;
import pl.bristleback.server.bristle.serialization.system.BristleSerializationResolver;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.SerializationException;
import pl.bristleback.server.bristle.utils.PropertyUtils;
import pl.bristleback.server.mock.beans.MockBean;
import pl.bristleback.server.mock.beans.SimpleMockBean;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;
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

  private double[] rawArray;
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
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "rawPropertyLong");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertEquals(332L, ((Long) deserialized).longValue());
  }

  @Test(expected = NumberFormatException.class)
  public void deserializeRawFormatException() throws Exception {
    //given
    String serializedForm = "33s2";
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "rawPropertyLong");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertEquals(332L, ((Long) deserialized).longValue());
  }

  @Test
  public void deserializeRawEmptyValue() throws Exception {
    //given
    String serializedForm = "";
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "rawPropertyLong");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertNull(deserialized);
  }

  @Test
  public void deserializeRawNullValue() throws Exception {
    //given
    String serializedForm = null;
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "rawPropertyLong");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertNull(deserialized);
  }

  @Test
  public void deserializeRawArray() throws Exception {
    //given
    String serializedForm = "[1.0, 2, 3.1]";
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "rawArray");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertTrue(deserialized instanceof double[]);
    double[] deserializedArray = (double[]) deserialized;
    assertEquals(1.0, deserializedArray[0]);
  }

  @Test
  public void deserializeRawCollection() throws Exception {
    //given
    String serializedForm = "[\"a\", \"b\", \"c\"]";
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "rawCollectionChar");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

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
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "beanCollectionChar");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

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
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "beanCollectionChar");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

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

    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "beanCollectionCharRequired");
    SerializationInput input = new SerializationInputResolver().resolveInputInformation(
      getClass().getDeclaredField("beanCollectionCharRequired").getAnnotation(Bind.class));
    PropertySerialization serialization = serializationResolver.resolveSerialization(type, input);

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

    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "notSimpleBeanCollectionFieldRequired");
    SerializationInput input = new SerializationInputResolver().resolveInputInformation(
      getClass().getDeclaredField("notSimpleBeanCollectionFieldRequired").getAnnotation(Bind.class));
    PropertySerialization serialization = serializationResolver.resolveSerialization(type, input);

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

    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "notSimpleBeanCollectionFieldRequired");
    SerializationInput input = new SerializationInputResolver().resolveInputInformation(
      getClass().getDeclaredField("notSimpleBeanCollectionFieldRequired").getAnnotation(Bind.class));
    PropertySerialization serialization = serializationResolver.resolveSerialization(type, input);

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
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "rawMap");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

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
    Type type = PropertyUtils.getDeclaredField(JsonFastDeserializerTest.class, "beanMap");
    PropertySerialization serialization = serializationResolver.resolveDefaultSerialization(type);

    //when
    Object deserialized = deserializer.deserialize(serializedForm, serialization);

    //then
    assertTrue(deserialized instanceof Map);
    Map<String, VerySimpleMockBean> deserializedList = (Map<String, VerySimpleMockBean>) deserialized;
    VerySimpleMockBean secondElement = deserializedList.get("b");
    assertEquals(22, secondElement.getSimpleField());
  }
}
