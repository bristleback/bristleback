package pl.bristleback.server.bristle.action;

import pl.bristleback.server.bristle.api.action.ActionParameterExtractor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-20 17:38:15 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ActionParameterInformation {

  private Type parameterType;
  private List<Annotation> parameterAnnotations;
  private Object propertySerialization;
  private ActionParameterExtractor extractor;

  public ActionParameterInformation(Type parameterType, Annotation[] parameterAnnotations) {
    this.parameterType = parameterType;
    this.parameterAnnotations = Arrays.asList(parameterAnnotations);
  }

  public Object resolveParameter(String message, ActionExecutionContext context) throws Exception {
    return extractor.fromTextContent(message, this, context);
  }

  public ActionParameterExtractor getExtractor() {
    return extractor;
  }

  public void setExtractor(ActionParameterExtractor extractor) {
    this.extractor = extractor;
  }

  public Object getPropertySerialization() {
    return propertySerialization;
  }

  public void setPropertySerialization(Object propertySerialization) {
    this.propertySerialization = propertySerialization;
  }

  public Type getParameterType() {
    return parameterType;
  }

  public List<Annotation> getParameterAnnotations() {
    return parameterAnnotations;
  }

  @Override
  public String toString() {
    return "Action parameter information, extractor: " + extractor + ", property Serialization: " + propertySerialization;
  }
}
