package pl.bristleback.server.bristle.utils;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-05 20:09:09 <br/>
 *
 * @author Wojciech Niemiec
 */
public final class StringUtils {

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

  public static String[] getPropertyChain(String fullPropertyPath) {
    return fullPropertyPath.split("\\" + StringUtils.DOT);
  }
}
