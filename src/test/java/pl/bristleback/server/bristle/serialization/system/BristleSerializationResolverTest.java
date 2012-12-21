package pl.bristleback.server.bristle.serialization.system;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.serialization.system.annotation.Bind;
import pl.bristleback.server.bristle.serialization.system.annotation.Property;
import pl.bristleback.server.bristle.serialization.system.json.extractor.BigIntegerValueSerializer;
import pl.bristleback.server.bristle.serialization.system.json.extractor.DateValueSerializer;
import pl.bristleback.server.bristle.serialization.system.json.extractor.DoubleValueSerializer;
import pl.bristleback.server.bristle.serialization.system.json.extractor.IntegerValueSerializer;
import pl.bristleback.server.bristle.serialization.system.json.extractor.LongValueSerializer;
import pl.bristleback.server.bristle.serialization.system.json.extractor.StringValueSerializer;
import pl.bristleback.server.mock.beans.MockBean;
import pl.bristleback.server.mock.beans.ParametrizedBean;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-08 17:03:14 <br/>
 *
 * @author Wojciech Niemiec
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class BristleSerializationResolverTest extends AbstractJUnit4SpringContextTests {
  private static Logger log = Logger.getLogger(BristleSerializationResolverTest.class.getName());

  private BristleSerializationResolver serializationResolver;

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private List<BigInteger> rawList;
  private List<MockBean> beanList;

  private Map<String, Date> rawMap;
  private Map<String, MockBean> beanMap;

  private Long[] rawArray;
  private MockBean[] beanArray;

  @Bind(properties = @Property(name = "simpleField", required = true))
  private VerySimpleMockBean beanFieldRequired;

  @Bind(properties = @Property(name = "simpleField", required = true))
  private List<VerySimpleMockBean> beanCollectionFieldRequired;

  @Bind(required = true)
  private double rawRequired;

  @Bind(format = "MM-dd")
  private Date rawDateFormatted;

  @Bind
  private Date rawDate;

  private ParametrizedBean<VerySimpleMockBean> parametrizedBean;

  @Before
  public void setUp() {
    serializationResolver = mockBeansFactory.getFrameworkBean("system.serializationResolver", BristleSerializationResolver.class);
  }


  private Field getField(String fieldName) throws NoSuchFieldException {
    return this.getClass().getDeclaredField(fieldName);
  }

  private Type getFieldType(String fieldName) throws NoSuchFieldException {
    return getField(fieldName).getGenericType();
  }

  @Test
  public void resolveDefaultSerializationRawTypeFirstTime() {
    //when
    PropertySerialization defaultRawTypeSerialization = serializationResolver.resolveSerialization(String.class);

    //then
    assertEquals(String.class, defaultRawTypeSerialization.getPropertyClass());
    assertEquals(StringValueSerializer.class, defaultRawTypeSerialization.getValueSerializer().getClass());
  }

  @Test
  public void resolveDefaultSerializationRawTypeSecondTime() {
    //given
    PropertySerialization defaultRawTypeSerialization1 = serializationResolver.resolveSerialization(String.class);

    //when
    PropertySerialization defaultRawTypeSerialization2 = serializationResolver.resolveSerialization(String.class);

    //then
    assertSame(defaultRawTypeSerialization1, defaultRawTypeSerialization2);
  }

  @Test
  public void resolveDefaultSerializationBeanType() {
    //when
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(MockBean.class);

    //then
    assertEquals(MockBean.class, beanSerialization.getPropertyClass());
    assertNotNull(beanSerialization.getPropertySerialization(MockBean.RAW_DOUBLE_PROPERTY));
    assertEquals(DoubleValueSerializer.class, beanSerialization.getPropertySerialization(MockBean.RAW_DOUBLE_PROPERTY).getValueSerializer().getClass());
    assertEquals(MockBean.FIELDS_COUNT, beanSerialization.getChildrenProperties().size());
  }

  @Test
  public void resolveDefaultSerializationCollectionType() throws Exception {
    //given
    rawList = new ArrayList<BigInteger>();
    Type listType = getFieldType("rawList");
    //when
    PropertySerialization listSerialization = serializationResolver.resolveSerialization(listType);
    PropertySerialization listElementSerialization = listSerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    //then
    assertNotNull(listElementSerialization);
    assertEquals(BigIntegerValueSerializer.class, listElementSerialization.getValueSerializer().getClass());
  }

  @Test
  public void resolveDefaultSerializationBeanCollectionType() throws Exception {
    //given
    beanList = new ArrayList<MockBean>();
    Type listType = getFieldType("beanList");
    //when
    PropertySerialization listSerialization = serializationResolver.resolveSerialization(listType);
    PropertySerialization listElementSerialization = listSerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    //then
    assertNotNull(listElementSerialization);
    assertNull(listElementSerialization.getValueSerializer());
    assertEquals(MockBean.class, listElementSerialization.getPropertyClass());
    assertEquals(MockBean.FIELDS_COUNT, listElementSerialization.getChildrenProperties().size());
  }

  @Test
  public void resolveDefaultSerializationRawMapType() throws Exception {
    //given
    rawMap = new HashMap<String, Date>();
    Type mapType = getFieldType("rawMap");
    //when
    PropertySerialization listSerialization = serializationResolver.resolveSerialization(mapType);
    PropertySerialization listElementSerialization = listSerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    //then
    assertNotNull(listElementSerialization);
    assertEquals(DateValueSerializer.class, listElementSerialization.getValueSerializer().getClass());
  }

  @Test
  public void resolveDefaultSerializationBeanMapType() throws Exception {
    //given
    beanMap = new HashMap<String, MockBean>();
    Type mapType = getFieldType("beanMap");
    //when
    PropertySerialization listSerialization = serializationResolver.resolveSerialization(mapType);
    PropertySerialization listElementSerialization = listSerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    //then
    assertNotNull(listElementSerialization);
    assertNull(listElementSerialization.getValueSerializer());
    assertEquals(MockBean.class, listElementSerialization.getPropertyClass());
    assertEquals(MockBean.FIELDS_COUNT, listElementSerialization.getChildrenProperties().size());
  }

  @Test
  public void resolveDefaultSerializationRawArrayType() throws Exception {
    //given
    rawArray = new Long[3];
    Type arrayType = getFieldType("rawArray");
    //when
    PropertySerialization listSerialization = serializationResolver.resolveSerialization(arrayType);
    PropertySerialization listElementSerialization = listSerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    //then
    assertNotNull(listElementSerialization);
    assertEquals(LongValueSerializer.class, listElementSerialization.getValueSerializer().getClass());
  }

  @Test
  public void resolveDefaultSerializationBeanArrayType() throws Exception {
    //given
    beanArray = new MockBean[3];
    Type arrayType = getFieldType("beanArray");
    //when
    PropertySerialization listSerialization = serializationResolver.resolveSerialization(arrayType);
    PropertySerialization listElementSerialization = listSerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    //then
    assertNotNull(listElementSerialization);
    assertNull(listElementSerialization.getValueSerializer());
    assertEquals(MockBean.class, listElementSerialization.getPropertyClass());
    assertEquals(MockBean.FIELDS_COUNT, listElementSerialization.getChildrenProperties().size());
  }

  @Test
  public void resolveSerializationRawRequired() throws Exception {
    //given
    Field beanField = getField("rawRequired");
    Type beanType = beanField.getGenericType();
    //when
    PropertySerialization rawSerialization = serializationResolver.resolveSerialization(beanType, beanField.getAnnotations());
    //then
    assertNotNull(rawSerialization);
    assertEquals(DoubleValueSerializer.class, rawSerialization.getValueSerializer().getClass());
    assertEquals(double.class, rawSerialization.getPropertyClass());
    assertTrue(rawSerialization.getConstraints().isRequired());
  }

  @Test
  public void resolveSerializationBeanOneFieldRequired() throws Exception {
    //given
    Field beanField = getField("beanFieldRequired");
    Type beanType = beanField.getGenericType();
    //when
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(beanType, beanField.getAnnotations());
    PropertySerialization beanElementSerialization = beanSerialization.getPropertySerialization("simpleField");
    //then
    assertNotNull(beanElementSerialization);
    assertEquals(IntegerValueSerializer.class, beanElementSerialization.getValueSerializer().getClass());
    assertEquals(VerySimpleMockBean.class, beanSerialization.getPropertyClass());
    assertEquals(int.class, beanElementSerialization.getPropertyClass());
    assertTrue(beanElementSerialization.getConstraints().isRequired());
  }

  @Test
  public void resolveSerializationBeanListOneFieldRequired() throws Exception {
    //given
    Field listField = getField("beanCollectionFieldRequired");
    Type listType = listField.getGenericType();
    //when
    PropertySerialization listSerialization = serializationResolver.resolveSerialization(listType, listField.getAnnotations());
    PropertySerialization listElementSerialization = listSerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    PropertySerialization beanElementSerialization = listElementSerialization.getPropertySerialization("simpleField");
    //then
    assertNotNull(listSerialization);
    assertNotNull(listElementSerialization);
    assertNotNull(beanElementSerialization);
    assertEquals(IntegerValueSerializer.class, beanElementSerialization.getValueSerializer().getClass());
    assertEquals(VerySimpleMockBean.class, listElementSerialization.getPropertyClass());
    assertEquals(int.class, beanElementSerialization.getPropertyClass());
    assertTrue(beanElementSerialization.getConstraints().isRequired());
  }

  @Test
  public void resolveSerializationRawDateFormatted() throws Exception {
    //given
    Field beanField = getField("rawDateFormatted");
    Type beanType = beanField.getGenericType();
    //when
    PropertySerialization rawSerialization = serializationResolver.resolveSerialization(beanType, beanField.getAnnotations());
    //then
    assertNotNull(rawSerialization);
    assertEquals(DateValueSerializer.class, rawSerialization.getValueSerializer().getClass());
    assertEquals(Date.class, rawSerialization.getPropertyClass());
    Assert.assertNotNull(rawSerialization.getConstraints().getFormat());
  }

  @Test
  public void resolveSerializationRawDateDefaultFormat() throws Exception {
    //given
    Field beanField = getField("rawDate");
    Type beanType = beanField.getGenericType();
    //when
    PropertySerialization rawSerialization = serializationResolver.resolveSerialization(beanType, beanField.getAnnotations());
    //then
    assertNotNull(rawSerialization);
    assertEquals(DateValueSerializer.class, rawSerialization.getValueSerializer().getClass());
    assertEquals(Date.class, rawSerialization.getPropertyClass());
    Assert.assertNotNull(rawSerialization.getConstraints().getFormat());
  }

  @Test
  public void resolveDefaultSerializationParametrizedBean() throws Exception {
    //given
    Type type = getFieldType("parametrizedBean");

    //when
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(type);
    PropertySerialization propertySerialization = beanSerialization.getPropertySerialization("parametrizedField1");

    //then
    assertNotNull(beanSerialization);
    assertNotNull(propertySerialization);
    assertEquals(VerySimpleMockBean.class, propertySerialization.getPropertyClass());
  }

  @Test
  public void resolveDefaultSerializationParametrizedBeanCollection() throws Exception {
    //given
    Type type = getFieldType("parametrizedBean");

    //when
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(type);
    PropertySerialization propertySerialization = beanSerialization.getPropertySerialization("parametrizedList");
    PropertySerialization listElementSerialization = propertySerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);

    //then
    assertNotNull(beanSerialization);
    assertNotNull(propertySerialization);
    assertEquals(PropertyType.COLLECTION, propertySerialization.getPropertyType());
    assertEquals(VerySimpleMockBean.class, listElementSerialization.getPropertyClass());
  }

  @Test
  public void resolveDefaultSerializationParametrizedBeanMap() throws Exception {
    //given
    Type type = getFieldType("parametrizedBean");

    //when
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(type);
    PropertySerialization propertySerialization = beanSerialization.getPropertySerialization("parametrizedMap");
    PropertySerialization listElementSerialization = propertySerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);

    //then
    assertNotNull(beanSerialization);
    assertNotNull(propertySerialization);
    assertEquals(PropertyType.MAP, propertySerialization.getPropertyType());
    assertEquals(VerySimpleMockBean.class, listElementSerialization.getPropertyClass());
  }

  @Test
  public void resolveDefaultSerializationParametrizedBeanArray() throws Exception {
    //given
    Type type = getFieldType("parametrizedBean");

    //when
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(type);
    PropertySerialization propertySerialization = beanSerialization.getPropertySerialization("parametrizedArray");
    PropertySerialization listElementSerialization = propertySerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);

    //then
    assertNotNull(beanSerialization);
    assertNotNull(propertySerialization);
    assertEquals(PropertyType.ARRAY, propertySerialization.getPropertyType());
    assertEquals(VerySimpleMockBean.class, listElementSerialization.getPropertyClass());
  }
}
