package pl.bristleback.server.bristle.conf.resolver.plain;

public class JavaComponentInformation {

  private Object instance;

  public JavaComponentInformation(Object instance) {
    this.instance = instance;
  }

  public Class<?> getComponentClass() {
    return instance.getClass();
  }

  public Object getInstance() {
    return instance;
  }
}
