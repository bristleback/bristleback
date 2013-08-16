package pl.bristleback.client.mockinstance;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.bristleback.server.bristle.app.StandaloneSpringServerRunner;

/**
 * <p/>
 * Created on: 31.07.13 21:08 <br/>
 *
 * @author Pawel Machowski
 */
public class BristlebacTestInstance {

  private static final String[] CONFIG_FILES =
    {"testApplicationContext.xml"};
  private StandaloneSpringServerRunner runner;


  public void startServer() {
    BasicConfigurator.configure();
    Logger.getLogger("org.apache").setLevel(Level.INFO);

    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONFIG_FILES);

    runner = (StandaloneSpringServerRunner) applicationContext.getBean("bristlebackStandaloneServer");

  }

  public void stopServer() {
    runner.stopServer();
  }


}
