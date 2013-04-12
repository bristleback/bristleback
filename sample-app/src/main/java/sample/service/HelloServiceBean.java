package sample.service;

import org.apache.log4j.Logger;

import java.math.BigDecimal;

public class HelloServiceBean {
  private static Logger log = Logger.getLogger(HelloServiceBean.class.getName());

  public String sayHello(BigDecimal number) {
    return "Hello! Magic number " + number;
  }
}
