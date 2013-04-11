package pl.bristleback.server.bristle.action;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-02-04 12:24:01 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum ActionExecutionStage {

  MESSAGE_DESERIALIZATION,
  ACTION_EXTRACTION,
  PARAMETERS_EXTRACTION,
  ACTION_EXECUTION,
  RESPONSE_CONSTRUCTION,

}
