package pl.bristleback.server.bristle.security.authentication;

/**
 * This enumeration informs user why his authentication has become invalidated.
 * <p/>
 * Created on: 26.03.13 20:29 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum LogoutReason {

  TOO_MANY_AUTHENTICATIONS,
  REQUESTED_BY_CLIENT
}
