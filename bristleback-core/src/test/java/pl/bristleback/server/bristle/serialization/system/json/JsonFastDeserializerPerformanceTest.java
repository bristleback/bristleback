package pl.bristleback.server.bristle.serialization.system.json;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
@Ignore
public class JsonFastDeserializerPerformanceTest extends BasePerformanceTest {

  @Test
  public void testRawSimpleValue() throws Exception {
    Locale.setDefault(Locale.ENGLISH);
    String serializedForm = "332221";
    Object serialization = getSerialization("rawLong");

    measurePerformance(serializedForm, serialization, "Long raw value");
  }

  @Test
  public void deserializeBigDecimalCustomFormatValue() throws Exception {
    //given
    Locale.setDefault(Locale.ENGLISH);
    String serializedForm = "332.221";
    Object serialization = getSerialization("rawCustomFormatBigDecimal");

    measurePerformance(serializedForm, serialization, "BigDecimal FORMATTED value");
  }

  @Test
  public void deserializeRawObjectArray() throws Exception {
    //given
    String serializedForm = "[1.0, 2, 3.1]";
    Object serialization = getSerialization("rawObjectArray");

    measurePerformance(serializedForm, serialization, "Double[] array (3 elements)");
  }

  @Test
  public void deserializeBeanCollection() throws Exception {
    //given
    String serializedForm = "[{\"simpleField\":1},{\"simpleField\":2},{\"simpleField\":3}]";
    Object serialization = getSerialization("beanCollection");

    measurePerformance(serializedForm, serialization, "List<VerySimpleMockBean> (3 elements)");
  }

  @Test
  public void deserializeRawMap() throws Exception {
    //given
    String serializedForm = "{\"a\":11, \"b\":22, \"c\":33}";
    Object serialization = getSerialization("rawMap");

    measurePerformance(serializedForm, serialization, "Map<String, Long> (3 elements)");
  }

  @Test
  public void deserializeBeanMap() throws Exception {
    //given
    String serializedForm = "{\"a\":{\"simpleField\":11}, \"b\":{\"simpleField\":22}, \"c\":{\"simpleField\":33}}";
    Object serialization = getSerialization("beanMap");

    measurePerformance(serializedForm, serialization, "Map<String, VerySimpleMockBean> (3 elements)");
  }

  @Test
  public void deserializeBristleMessage() throws Exception {
    //given
    String serializedForm = "{\"id\":12, \"name\":\"actionClass.action\", \"payload\":{\"simpleField\":22}}";
    Object serialization = getSerialization("bristleMessage");

    measurePerformance(serializedForm, serialization, "BristleMessage");
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void performTests(Object serializedForm, Object serialization, int iterations) throws Exception {
    String stringToDeserialize = (String) serializedForm;
    for (int i = 0; i < iterations; i++) {
      Object deserialized = serializer.deserialize(stringToDeserialize, serialization);
    }
  }
}