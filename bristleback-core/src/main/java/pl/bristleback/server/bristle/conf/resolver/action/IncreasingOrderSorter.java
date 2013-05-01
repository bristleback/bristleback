package pl.bristleback.server.bristle.conf.resolver.action;

import org.springframework.core.annotation.Order;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class IncreasingOrderSorter<T> {

  public void sort(List<T> list) {
    Comparator<T> comparator = new Comparator<T>() {
      @Override
      public int compare(T object1, T object2) {
        Order annotation1 = getSortedClass(object1).getAnnotation(Order.class);
        Order annotation2 = getSortedClass(object2).getAnnotation(Order.class);
        if (annotation2 == null) {
          return -1;
        }
        if (annotation1 == null) {
          return 1;
        }
        if (annotation1.value() > annotation2.value()) {
          return 1;
        }
        return -1;
      }

    };
    Collections.sort(list, comparator);
  }

  public abstract Class<?> getSortedClass(T object);
}
