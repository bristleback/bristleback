package pl.bristleback.server.bristle.validation;


import javax.validation.ConstraintViolation;
import java.util.Set;

public class ActionValidationException extends RuntimeException {

  private Set<ConstraintViolation<Object>> validationResults;

  public ActionValidationException(Set<ConstraintViolation<Object>> validationResults) {
    this.validationResults = validationResults;
  }

  public Set<ConstraintViolation<Object>> getValidationResults() {
    return validationResults;
  }
}
