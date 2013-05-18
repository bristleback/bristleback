package pl.bristleback.server.bristle.serialization;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.bristleback.server.bristle.serialization.system.BristleSerializationResolver;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;
import pl.bristleback.server.bristle.serialization.system.json.extractor.StringValueSerializer;
import pl.bristleback.server.mock.beans.MockBean;
import pl.bristleback.server.mock.beans.SimpleMockBean;
import pl.bristleback.server.mock.beans.SpringMockBeansFactory;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-context.xml"})
public class BristleJsonSerializationResolverTest {

  private BristleSerializationResolver serializationResolver;

  @Inject
  private SpringMockBeansFactory mockBeansFactory;

  @Inject
  private ApplicationContext applicationContext;

  @Before
  public void setUp() {
    serializationResolver = mockBeansFactory.getFrameworkBean("system.serializationResolver", BristleSerializationResolver.class);
  }

  @Test
  public void testRawTypeSerializationFirstTime() {
    PropertySerialization defaultRawTypeSerialization = serializationResolver.resolveSerialization(String.class);
    assertEquals(String.class, defaultRawTypeSerialization.getPropertyClass());
    assertEquals(StringValueSerializer.class, defaultRawTypeSerialization.getValueSerializer().getClass());
  }

  @Test
  public void testBeanDefaultSerialization() {
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(MockBean.class);
    assertEquals(MockBean.class, beanSerialization.getPropertyClass());
  }

  @Test
  public void testBeanDefaultSerializationCollection() {
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(MockBean.class);
    PropertySerialization listSerialization = beanSerialization.getPropertySerialization(MockBean.LIST_OF_STRINGS_PROPERTY);
    assertNotNull(listSerialization);
    assertNotNull(listSerialization.getGenericType());
    PropertySerialization listElementSerialization = listSerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    assertNotNull(listElementSerialization);
    assertEquals(String.class, listElementSerialization.getPropertyClass());
  }

  @Test
  public void testBeanDefaultSerializationMap() {
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(MockBean.class);
    PropertySerialization mapSerialization = beanSerialization.getPropertySerialization(MockBean.MAP_OF_DOUBLES_PROPERTY);
    assertNotNull(mapSerialization);
    assertNotNull(mapSerialization.getGenericType());
    PropertySerialization mapElementSerialization = mapSerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    assertNotNull(mapElementSerialization);
    assertEquals(Double.class, mapElementSerialization.getPropertyClass());
  }

  @Test
  public void testBeanDefaultSerializationArrayOfBeans() {
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(MockBean.class);
    PropertySerialization arraySerialization = beanSerialization.getPropertySerialization(MockBean.ARRAY_OF_BEANS_PROPERTY);
    assertNotNull(arraySerialization);
    PropertySerialization arrayElementSerialization = arraySerialization.getPropertySerialization(PropertySerialization.CONTAINER_ELEMENT_PROPERTY_NAME);
    assertNotNull(arrayElementSerialization);
    assertEquals(SimpleMockBean.class, arrayElementSerialization.getPropertyClass());
    int expectedPropertiesMapSize = 2;
    assertEquals(expectedPropertiesMapSize, arrayElementSerialization.getPropertiesInformation().size());
    assertEquals(int.class, arrayElementSerialization.getPropertySerialization(SimpleMockBean.RAW_INT_PROPERTY).getPropertyClass());

  }

  @Test
  public void testBeanDefaultSerializationNestedBean() {
    PropertySerialization beanSerialization = serializationResolver.resolveSerialization(MockBean.class);
    PropertySerialization nestedBeanSerialization = beanSerialization.getPropertySerialization(MockBean.SIMPLE_MOCK_BEAN_PROPERTY);
    assertNotNull(nestedBeanSerialization);
    assertEquals(SimpleMockBean.class, nestedBeanSerialization.getPropertyClass());
    int expectedPropertiesMapSize = 2;
    assertEquals(expectedPropertiesMapSize, nestedBeanSerialization.getPropertiesInformation().size());
    assertEquals(int.class, nestedBeanSerialization.getPropertySerialization(SimpleMockBean.RAW_INT_PROPERTY).getPropertyClass());
  }

}
