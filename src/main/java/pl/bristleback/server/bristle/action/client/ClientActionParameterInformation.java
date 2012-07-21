package pl.bristleback.server.bristle.action.client;

import java.lang.reflect.Type;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-07-17 20:41:06 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionParameterInformation {

  private Type parameterType;
  private boolean forSerialization;

  public ClientActionParameterInformation(Type parameterType, boolean forSerialization) {
    this.parameterType = parameterType;
    this.forSerialization = forSerialization;
  }

  public Type getParameterType() {
    return parameterType;
  }

  public boolean isForSerialization() {
    return forSerialization;
  }

  @Override
  public String toString() {
    return "client action parameter, type: " + parameterType + ", for serialization: " + forSerialization;
  }
}
