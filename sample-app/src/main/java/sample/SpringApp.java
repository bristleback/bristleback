package sample;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.bristleback.server.bristle.app.StandaloneSpringServerRunner;

import java.util.Scanner;

/**
 * Sample application used for development of new features
 * Hello world!
 */
public final class SpringApp {

  private static final String[] CONFIG_FILES =
    {"applicationContext.xml"};

  private SpringApp() {
  }

  public static void main(String[] args) {
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONFIG_FILES);

    StandaloneSpringServerRunner runner = (StandaloneSpringServerRunner) applicationContext.getBean("bristlebackStandaloneServer");

    Scanner in = new Scanner(System.in);
    String value = in.nextLine();
    while (!value.equalsIgnoreCase("x")) {
      value = in.nextLine();
    }
    runner.stopServer();
  }
}
