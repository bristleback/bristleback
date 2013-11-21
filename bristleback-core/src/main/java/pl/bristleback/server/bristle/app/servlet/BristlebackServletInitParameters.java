package pl.bristleback.server.bristle.app.servlet;

import org.apache.commons.lang.StringUtils;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BristlebackServletInitParameters {

  private Map<String, String> parameters = new HashMap<String, String>();

  public BristlebackServletInitParameters(Map<String, String> initParams) {
    this.parameters = initParams;
  }

  public String getParam(String name) {
    return parameters.get(name);
  }

  public Integer getIntParam(String name) {
    if (!parameters.containsKey(name)) {
      return null;
    }
    return Integer.parseInt(parameters.get(name));
  }

  @SuppressWarnings("unchecked")
  public List<String> getListParam(String name) {
    if (!parameters.containsKey(name)) {
      return null;
    }
    String rawValue = getParam(name);
    List<String> result = new ArrayList<String>();
    for (String notTrimmed : StringUtils.split(rawValue, ",")) {
      result.add(notTrimmed.trim());
    }

    return result;
  }

  public Class<?> getClassParam(String name) {
    try {
      if (!parameters.containsKey(name)) {
        return null;
      }
      return Class.forName(name);
    } catch (ClassNotFoundException e) {
      throw new BristleInitializationException("Cannot find class with name: " + name, e);
    }
  }
}
