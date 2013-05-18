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
