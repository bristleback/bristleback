package pl.bristleback.server.bristle.message.outgoing;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.authorisation.user.UsersContainer;

import javax.annotation.PostConstruct;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-26 12:07:38 <br/>
 *
 * @author Wojciech Niemiec
 */
public class OutgoingMessageProxyInterceptor implements MethodInterceptor {
  private static Logger log = Logger.getLogger(OutgoingMessageProxyInterceptor.class.getName());

  private BristlebackConfig configuration;

  private UsersContainer usersContainer;
  private ClientActionClasses actionClasses;

  private ClientActionInformation actionInformation;

  @PostConstruct
  private void init() {
    usersContainer = configuration.getSpringIntegration().getFrameworkBean("usersContainer", UsersContainer.class);
    actionClasses = configuration.getSpringIntegration().getFrameworkBean("clientActionClasses", ClientActionClasses.class);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    if (actionInformation == null) {
      resolveActionInformation(invocation);
    }

    return null;
  }

  private void resolveActionInformation(MethodInvocation invocation) {
    ClientActionClassInformation actionClassInformation = actionClasses.getOutgoingActionClass(invocation.getClass());
    actionInformation = actionClassInformation.getClientAction(invocation.getMethod());
  }

  public void setConfiguration(BristlebackConfig configuration) {
    this.configuration = configuration;
  }
}
