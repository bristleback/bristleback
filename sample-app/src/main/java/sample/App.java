package sample;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.bristleback.server.bristle.app.StandaloneServerRunner;

import java.util.Scanner;

/**
 * Sample application used for development of new features
 * Hello world!
 */
public final class App {

  private static final String[] CONFIG_FILES =
    {"applicationContext.xml"};

  private App() {
  }

  public static void main(String[] args) {
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONFIG_FILES);

    StandaloneServerRunner runner = (StandaloneServerRunner) applicationContext.getBean("bristlebackStandaloneServer");

    Scanner in = new Scanner(System.in);
    String value = in.nextLine();
    while (!value.equalsIgnoreCase("x")) {
      value = in.nextLine();
    }
    runner.stopServer();
  }
}
