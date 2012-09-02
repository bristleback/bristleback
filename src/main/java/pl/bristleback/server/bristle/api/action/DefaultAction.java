package pl.bristleback.server.bristle.api.action;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * Classes implementing this interface contain default actions ready to be executed by clients.
 * This interface should be implemented by action classes (classes marked with {@link pl.bristleback.server.bristle.api.annotations.AnnotatedActionClass} annotation).
 * This is optional interface, default actions are not required in every action classes.
 * However, they might be faster because reflection is not used to invoke them.
 * Default actions are way less flexible than normal actions, they always take two parameters:
 * current user and a payload of the incoming message.  <br/>
 * <strong>As every action, default action method must be annotated with {@link pl.bristleback.server.bristle.api.annotations.Action Action}.</strong> <br/>
 * Default action method may return any type (but return type must be specified in method signature),
 * set return type to {@link Void} if no response should be sent to client.
 * <p/>
 * Created on: 2011-07-20 12:29:18 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface DefaultAction<U extends IdentifiedUser, T> {

  /**
   * Executes default action of implementing action class.
   *
   * @param user    current user implementation.
   * @param payload custom type payload object.
   * @return response.
   * @throws Exception if any exception while action invocation occurs.
   *                   All exceptions are handled by the Action Controller.
   */
  Object executeDefault(U user, T payload) throws Exception;
} 
