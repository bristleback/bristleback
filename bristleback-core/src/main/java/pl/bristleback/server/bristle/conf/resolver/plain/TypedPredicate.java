package pl.bristleback.server.bristle.conf.resolver.plain;

public interface TypedPredicate<E> {

  boolean evaluate(E element);
}
