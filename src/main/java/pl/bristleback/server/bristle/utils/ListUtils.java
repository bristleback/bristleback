package pl.bristleback.server.bristle.utils;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-08-09 14:16:49 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class ListUtils {
  private static Logger log = Logger.getLogger(ListUtils.class.getName());

  private ListUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean isEmpty(List list) {
    return list == null || list.isEmpty();
  }

  public static boolean isNotEmpty(List list) {
    return list != null && !list.isEmpty();
  }
} 