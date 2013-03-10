package pl.bristleback.server.bristle.exceptions;

/**
 * This exception is thrown when single connection tries to authenticate
 * but there is still valid authentication assigned to this connection.
 * <p/>
 * Created on: 09.03.13 14:03 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserAlreadyAuthenticatedException extends RuntimeException {

}
