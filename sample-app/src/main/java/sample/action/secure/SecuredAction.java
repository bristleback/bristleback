package sample.action.secure;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.api.annotations.Authorized;
import sample.security.SampleUserDetails;

@ActionClass
@Component
@Authorized
public class SecuredAction {

  private static Logger log = Logger.getLogger(SecuredAction.class.getName());

  @Action
  public void runSecuredAction(SampleUserDetails userDetails) {
    log.debug("running secured action invoked for " + userDetails.getUsername());
  }

  @Action
  @Authorized("admin")
  public void runAdminAction() {
    log.debug("running secured action requiring admin privileges");
  }
}
