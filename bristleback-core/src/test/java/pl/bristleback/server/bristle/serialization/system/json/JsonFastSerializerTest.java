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
import pl.bristleback.server.bristle.serialization.system.annotation.Serialize;
import pl.bristleback.server.bristle.utils.PropertyUtils;
import pl.bristleback.server.mock.beans.AbstractBean;
import pl.bristleback.server.mock.beans.ImplementationBean;
import pl.bristleback.server.mock.beans.InterfaceForBean;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;
import pl.bristleback.server.mock.beans.VerySimpleMockBean;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class JsonFastSerializerTest extends AbstractJUnit4SpringContextTests {

  private static Logger log = Logger.getLogger(JsonFastSerializerTest.class.getName());

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  private JsonFastSerializer fastSerializer;

  private BristleSerializationResolver serializationResolver;

  @Serialize(format = "yyyy-MM-dd HH:mm")
  private Date rawCustomFormatDate;

  private Date rawDate;

  @Serialize(format = "0.0000")
  private BigDecimal rawCustomFormatBigDecimal;

  @Serialize(format = "###,###,000,000,000,000,000")
  private BigDecimal rawCustomFormatBigInteger;

  @Serialize(format = "0.0000")
  private Double rawCustomFormatDouble;

  private Double rawDouble;

  @Serialize(format = "0,000")
  private int rawCustomFormatInteger;

  private int rawInteger;

  @Serialize(format = "000,000,000,000")
  private long rawCustomFormatLong;

  private Long rawLong;

  private double[] rawArray;

  private VerySimpleMockBean[] beanArray;

  private List<Integer> sampleList;

  private List<VerySimpleMockBean> beanList;

  private Map<String, BigDecimal> rawMap;

  private InterfaceForBean<VerySimpleMockBean> interfaceForBean;

  private AbstractBean<VerySimpleMockBean> abstractBean;

  @Before
  public void setUp() {
    fastSerializer = mockBeansFactory.getFrameworkBean("jsonSerializer.fastSerializer", JsonFastSerializer.class);
    serializationResolver = mockBeansFactory.getFrameworkBean("system.serializationResolver", BristleSerializationResolver.class);
  }

  @Test
  public void serializeRawValue() throws Exception {
    Integer four = 4;
    PropertySerialization serialization = serializationResolver.resolveSerialization(four.getClass());
    String result = fastSerializer.serializeObject(four, serialization);
    assertEquals(four + "", result);
  }

  @Test
  public void serializeRawNotFormattedDate() throws Exception {
    //given
    PropertySerialization serialization = serializationResolver.resolveSerialization(Date.class);
    Date dateToSerialize = new Date();
    //when
    String result = fastSerializer.serializeObject(dateToSerialize, serialization);

    //then
    String expectedResult = dateToSerialize.getTime() + "";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawFormattedDate() throws Exception {
    //given
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 22);
    calendar.set(Calendar.MINUTE, 45);
    calendar.set(Calendar.DAY_OF_MONTH, 2);
    calendar.set(Calendar.MONTH, 3);
    calendar.set(Calendar.YEAR, 2003);
    PropertySerialization serialization = serializationResolver.resolveSerialization(Date.class, getFieldsAnnotations("rawCustomFormatDate"));

    //when
    String result = fastSerializer.serializeObject(calendar.getTime(), serialization);

    //then
    String expectedResult = "\"2003-04-02 22:45\"";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawFormattedBigDecimal() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    BigDecimal number = new BigDecimal("3.12");
    PropertySerialization serialization = serializationResolver.resolveSerialization(BigDecimal.class, getFieldsAnnotations("rawCustomFormatBigDecimal"));

    //when
    String result = fastSerializer.serializeObject(number, serialization);

    //then
    String expectedResult = "\"3.1200\"";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawFormattedBigInteger() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    BigInteger number = new BigInteger("77383793873897398");
    PropertySerialization serialization = serializationResolver.resolveSerialization(BigInteger.class, getFieldsAnnotations("rawCustomFormatBigInteger"));

    //when
    String result = fastSerializer.serializeObject(number, serialization);

    //then
    String expectedResult = "\"77,383,793,873,897,398\"";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawFormattedDouble() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    double doubleNumber = 3.12;
    PropertySerialization serialization = serializationResolver.resolveSerialization(Double.class, getFieldsAnnotations("rawCustomFormatDouble"));

    //when
    String result = fastSerializer.serializeObject(doubleNumber, serialization);

    //then
    String expectedResult = "\"3.1200\"";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawNotFormattedDouble() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    double doubleNumber = 3.12;
    PropertySerialization serialization = serializationResolver.resolveSerialization(Double.class, getFieldsAnnotations("rawDouble"));

    //when
    String result = fastSerializer.serializeObject(doubleNumber, serialization);

    //then
    String expectedResult = "3.12";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawFormattedInteger() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    int intNumber = 3000;
    PropertySerialization serialization = serializationResolver.resolveSerialization(Integer.class, getFieldsAnnotations("rawCustomFormatInteger"));

    //when
    String result = fastSerializer.serializeObject(intNumber, serialization);

    //then
    String expectedResult = "\"3,000\"";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawNotFormattedInteger() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    int intNumber = 3000;
    PropertySerialization serialization = serializationResolver.resolveSerialization(Integer.class, getFieldsAnnotations("rawInteger"));

    //when
    String result = fastSerializer.serializeObject(intNumber, serialization);

    //then
    String expectedResult = "3000";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawFormattedLong() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    Long longNumber = 30000000L;
    PropertySerialization serialization = serializationResolver.resolveSerialization(Long.class, getFieldsAnnotations("rawCustomFormatLong"));

    //when
    String result = fastSerializer.serializeObject(longNumber, serialization);

    //then
    String expectedResult = "\"000,030,000,000\"";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawNotFormattedLong() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    long longNumber = 3000L;
    PropertySerialization serialization = serializationResolver.resolveSerialization(long.class, getFieldsAnnotations("rawLong"));

    //when
    String result = fastSerializer.serializeObject(longNumber, serialization);

    //then
    String expectedResult = "3000";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeRawArray() throws Exception {
    int size = 4;
    rawArray = new double[size];
    for (int i = 1; i <= size; i++) {
      rawArray[i - 1] = (double) i;
    }
    Type listType = PropertyUtils.getDeclaredFieldType(JsonFastSerializerTest.class, "rawArray");
    PropertySerialization serialization = serializationResolver.resolveSerialization(listType);
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
    Type listType = PropertyUtils.getDeclaredFieldType(JsonFastSerializerTest.class, "beanArray");
    PropertySerialization serialization = serializationResolver.resolveSerialization(listType);
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
    Type listType = PropertyUtils.getDeclaredFieldType(JsonFastSerializerTest.class, "sampleList");
    PropertySerialization serialization = serializationResolver.resolveSerialization(listType);
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
    Type listType = PropertyUtils.getDeclaredFieldType(JsonFastSerializerTest.class, "beanList");
    PropertySerialization serialization = serializationResolver.resolveSerialization(listType);
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
    Type listType = PropertyUtils.getDeclaredFieldType(JsonFastSerializerTest.class, "rawMap");
    PropertySerialization serialization = serializationResolver.resolveSerialization(listType);
    String result = fastSerializer.serializeObject(rawMap, serialization);
    String expectedResult = "{\"1s\":1,\"2s\":2}";
    assertEquals(expectedResult, result);
  }

  @Test
  public void serializeInterfaceImplementation() throws Exception {
    ImplementationBean implementationBean = new ImplementationBean();
    VerySimpleMockBean beanToSerialize = new VerySimpleMockBean();
    beanToSerialize.setSimpleField(2);
    implementationBean.setObject(beanToSerialize);
    implementationBean.setAdditionalField("someValue");

    Type listType = PropertyUtils.getDeclaredFieldType(JsonFastSerializerTest.class, "interfaceForBean");
    PropertySerialization serialization = serializationResolver.resolveSerialization(listType);
    String result = fastSerializer.serializeObject(implementationBean, serialization);
    String expectedResult = "{\"additionalField\":\"someValue\",\"object\":{\"simpleField\":2}}";
    assertEquals(expectedResult, result);
  }

  @Test
    public void serializeAbstractBeanImplementation() throws Exception {
      ImplementationBean implementationBean = new ImplementationBean();
      VerySimpleMockBean beanToSerialize = new VerySimpleMockBean();
      beanToSerialize.setSimpleField(2);
      implementationBean.setObject(beanToSerialize);
      implementationBean.setAdditionalField("someValue");

      Type listType = PropertyUtils.getDeclaredFieldType(JsonFastSerializerTest.class, "abstractBean");
      PropertySerialization serialization = serializationResolver.resolveSerialization(listType);
      String result = fastSerializer.serializeObject(implementationBean, serialization);
      String expectedResult = "{\"additionalField\":\"someValue\",\"object\":{\"simpleField\":2}}";
      assertEquals(expectedResult, result);
    }

  private Annotation[] getFieldsAnnotations(String fieldName) throws NoSuchFieldException {
    return getClass().getDeclaredField(fieldName).getAnnotations();
  }
} 