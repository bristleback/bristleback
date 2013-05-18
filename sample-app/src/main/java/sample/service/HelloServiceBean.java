package sample.service;

import java.math.BigDecimal;

public class HelloServiceBean {

  public String sayHello(BigDecimal number) {
    return "Hello! Magic number " + number;
  }
}
