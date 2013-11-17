package pl.bristleback.server.bristle.app.servlet;


import org.apache.commons.lang.StringUtils;
import pl.bristleback.server.bristle.api.ServletServerInstanceResolver;
import pl.bristleback.server.bristle.app.BristlebackServerInstance;
import pl.bristleback.server.bristle.conf.BristleInitializationException;

import javax.servlet.ServletConfig;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BristlebackServletManager {

  private static final String CONFIGURATION_RESOLVER_CLASS = "resolverClass";

  public BristlebackServerInstance createInitialConfigurationResolver(ServletConfig servletConfig) {
    final BristlebackServletInitParameters initParameters = resolveInitParameters(servletConfig);
    final ServletServerInstanceResolver servletWebsocketServerResolver = getServletConfigurationResolver(initParameters);
    return servletWebsocketServerResolver.createServerInstance(initParameters);
  }

  private BristlebackServletInitParameters resolveInitParameters(ServletConfig servletConfig) {
    Map<String, String> initParams = new HashMap<String, String>();
    Enumeration initParamNames = servletConfig.getInitParameterNames();
    while (initParamNames.hasMoreElements()) {
      String paramName = (String) initParamNames.nextElement();
      initParams.put(paramName, servletConfig.getInitParameter(paramName));
    }

    return new BristlebackServletInitParameters(initParams);
  }

  private ServletServerInstanceResolver getServletConfigurationResolver(BristlebackServletInitParameters initParameters) {
    String resolverCLassName = initParameters.getParam(CONFIGURATION_RESOLVER_CLASS);
    if (StringUtils.isBlank(resolverCLassName)) {
      throw new BristleInitializationException("Cannot find Bristleback servlet parameter: " + CONFIGURATION_RESOLVER_CLASS);
    }
    try {
      return (ServletServerInstanceResolver) Class.forName(resolverCLassName).newInstance();
    } catch (InstantiationException e) {
      throw new BristleInitializationException("Cannot create " + ServletServerInstanceResolver.class.getSimpleName() + " implementation, "
        + "cannot create an instance of " + resolverCLassName);
    } catch (IllegalAccessException e) {
      throw new BristleInitializationException("Cannot access " + ServletServerInstanceResolver.class.getSimpleName() + " implementation, "
        + "cannot access " + resolverCLassName);
    } catch (ClassNotFoundException e) {
      throw new BristleInitializationException("Cannot find " + ServletServerInstanceResolver.class.getSimpleName() + " implementation, "
        + "class " + resolverCLassName + " cannot be found");
    }
  }
}
