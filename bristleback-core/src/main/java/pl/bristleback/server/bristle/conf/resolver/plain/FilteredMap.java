package pl.bristleback.server.bristle.conf.resolver.plain;

import java.util.HashMap;
import java.util.Map;

public final class FilteredMap {

  private FilteredMap() {
    throw new UnsupportedOperationException();
  }

  public static <K, V> Map<K, V> filteredValues(Map<K, V> map, TypedPredicate<V> predicate) {
    Map<K, V> result = new HashMap<K, V>();

    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (predicate.evaluate(entry.getValue())) {
        result.put(entry.getKey(), entry.getValue());
      }
    }

    return result;
  }
}
