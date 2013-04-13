package pl.bristleback.server.bristle.security;

import pl.bristleback.server.bristle.action.ActionExecutionContext;
import pl.bristleback.server.bristle.action.ActionParameterInformation;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;
import pl.bristleback.server.bristle.api.users.UserDetails;
import pl.bristleback.server.bristle.security.authentication.AuthenticationsContainer;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This action extractor returns instance of {@link UserDetails} implementation assigned for current connection.
 * If this connection is not authenticated, or authentication is not valid,
 * {@link pl.bristleback.server.bristle.security.exception.UserNotAuthenticatedException} exception is thrown.
 * <p/>
 * Created on: 13.04.13 17:50 <br/>
 *
 * @author Wojciech Niemiec
 */
public class UserDetailsParameterExtractor implements ActionParameterExtractor<UserDetails> {

  @Inject
  @Named("bristleAuthenticationsContainer")
  private AuthenticationsContainer authenticationsContainer;

  @Override
  public void init(BristlebackConfig configuration) {
  }

  @Override
  public UserDetails fromTextContent(String text, ActionParameterInformation parameterInformation, ActionExecutionContext context) throws Exception {
    return authenticationsContainer.getAuthentication(context.getUserContext().getId()).getAuthenticatedUser();
  }

  @Override
  public boolean isDeserializationRequired() {
    return false;
  }
}
