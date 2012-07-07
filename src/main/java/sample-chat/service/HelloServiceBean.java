package sample.service;

import org.apache.log4j.Logger;

import java.math.BigDecimal;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-24 19:48:20 <br/>
 *
 * @author Wojciech Niemiec
 */
public class HelloServiceBean {
  private static Logger log = Logger.getLogger(HelloServiceBean.class.getName());

  public String sayHello(BigDecimal number) {
    return "Hello! Magic number " + number;
  }
}
