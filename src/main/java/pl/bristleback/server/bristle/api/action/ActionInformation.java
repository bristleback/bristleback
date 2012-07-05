package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.action.response.ActionResponseInformation;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 17:54:14 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface ActionInformation<T> {

  boolean isDefaultAction();

  String getName();

  List<ActionParameterInformation> getParameters();

  Object execute(T actionClass, Object[] parameters) throws Exception;

  void setParameters(List<ActionParameterInformation> parameters);

  ActionResponseInformation getResponseInformation();

  void setResponseInformation(ActionResponseInformation responseInformation);

} 
