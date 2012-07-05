package pl.bristleback.server.bristle.authorisation;

/**
 * Pawel Machowski
 * Date: 11.02.12
 */
public interface AuthorisationPolicy {

  boolean isActionAuthorised(String action);
}
