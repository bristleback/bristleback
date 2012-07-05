package pl.bristleback.server.bristle.authorisation;

import org.springframework.stereotype.Component;

/**
 * Pawel Machowski
 * created at 11.02.12 14:52
 */
@Component
public class ActionNameAuthorisationPolicy implements AuthorisationPolicy {


  private AuthorisedActions authorisedActions;

  @Override
  public boolean isActionAuthorised(String action) {
    return false;
  }
}
