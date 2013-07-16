package sample.action;

import org.springframework.stereotype.Controller;
import pl.bristleback.server.bristle.api.action.DefaultAction;
import pl.bristleback.server.bristle.api.annotations.Action;
import pl.bristleback.server.bristle.api.annotations.ActionClass;
import pl.bristleback.server.bristle.engine.user.BaseUserContext;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@ActionClass(name = "HelloWorld")
public class HelloWorldAction implements DefaultAction<BaseUserContext, Map<String, BigDecimal>> {

  @Action
  public String executeDefault(BaseUserContext userContext, Map<String, BigDecimal> message) {
    return "response";
  }

}
