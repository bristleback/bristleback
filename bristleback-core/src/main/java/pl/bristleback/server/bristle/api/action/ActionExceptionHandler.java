package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionExecutionStage;

/**
 * This interface should be implemented when special reaction for more or less specified exception is required.
 * Every time exception occurs in action execution, special handlers container searches for most suitable exception handler.
 * Each handler defines action stages covered. Moreover, handlers are parametrized,
 * type of parameter determines the most general type of exception that will be processed by this handler.
 * Only first found handler is run by the container.
 * <p/>
 * Created on: 2012-02-04 14:49:32 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.action.ActionExecutionStage
 */
public interface ActionExceptionHandler<T extends Exception> {

  /**
   * Handles occurring exception. Returned object will be serialized and send as a response.
   * Although this method may return objects of any type,
   * it is recommended to return {@link pl.bristleback.server.bristle.action.response.ExceptionResponse} (or its subclasses),
   * as this type is recognised by Action Controller (special exception response is sent when ExceptionResponse object is returned).
   *
   * @param exception exception thrown during action execution.
   * @param context   current action invocation context.
   * @return Response object that will be sent back to client.
   *         {@link pl.bristleback.server.bristle.action.response.ExceptionResponse ExceptionResponse} is recommended.
   * @see pl.bristleback.server.bristle.action.response.ExceptionResponse
   */
  Object handleException(T exception, ActionExecutionContext context);

  /**
   * Points out action stages in which this exception handler operates.
   * Even if exception type matches parameter type of given handler, exception won't be processed
   * if occurred in action stage not listed in this method.
   *
   * @return action execution stages covered by this action exception handler.
   */
  ActionExecutionStage[] getHandledStages();
}
