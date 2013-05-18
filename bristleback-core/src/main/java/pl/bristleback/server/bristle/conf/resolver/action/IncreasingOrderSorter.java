/*
 * Bristleback Websocket Framework - Copyright (c) 2010-2013 http://bristleback.pl
 * ---------------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of merchantability
 * or fitness for a particular purpose.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
 * ---------------------------------------------------------------------------
 */

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
