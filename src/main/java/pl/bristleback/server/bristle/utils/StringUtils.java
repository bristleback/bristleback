package pl.bristleback.server.bristle.utils;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-05 20:09:09 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class StringUtils {
  private static Logger log = Logger.getLogger(StringUtils.class.getName());

  public static final String EMPTY = "";
  public static final char DOT = '.';
  public static final String DOT_AS_STRING = ".";
  public static final char COMMA = ',';
  public static final char COLON = ':';
  public static final char LITERAL_MARK = '"';
  public static final char LEFT_CURLY = '{';
  public static final char RIGHT_CURLY = '}';
  public static final char LEFT_BRACKET = '[';
  public static final char RIGHT_BRACKET = ']';

  private StringUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * URL-decodes {@code value} using the UTF-8 charset. Using this method eliminates the need for
   * a try/catch since UTF-8 is guaranteed to exist.
   *
   * @see java.net.URLDecoder#decode(String, String)
   */
  public static String urlDecode(String value) {
    try {
      return URLDecoder.decode(value, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Unsupported encoding?  UTF-8?  That's unpossible.", e);
    }
  }

  public static String[] getPropertyChain(String fullPropertyPath) {
    return fullPropertyPath.split("\\" + StringUtils.DOT);
  }
}
