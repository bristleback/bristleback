package pl.bristleback.server.bristle.app.servlet;


import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BristlebackServletInitParametersTest {


  @Test
  public void shouldProperlyGetArrayOfParameters() {
    //given
    Map<String, String> parametersMap = new HashMap<String, String>();
    parametersMap.put("key", "value1, value2,value3");
    BristlebackServletInitParameters initParameters = new BristlebackServletInitParameters(parametersMap);

    //when
    List<String> result = initParameters.getListParam("key");

    //then
    assertEquals(3, result.size());
    assertEquals("value1", result.get(0));
    assertEquals("value2", result.get(1));
    assertEquals("value3", result.get(2));
  }
}
