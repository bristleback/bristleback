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

package pl.bristleback.server.bristle.serialization.system.converter;

import pl.bristleback.server.bristle.serialization.system.DeserializationException;

/**
 * This utility class contains methods for traversing through json text.
 * <p/>
 * Created on: 27.12.12 19:01 <br/>
 *
 * @author Wojciech Niemiec
 */
public class JsonTokenizer {

  private static final int HEX_RADIX = 16;

  private boolean takeNextValueFromLastRead;

  private String jsonAsText;

  private int index = -1;
  private int lastTokenBeginIndex = -1;

  private String lastTokenValue;

  private JsonTokenType lastTokenType;

  /**
   * Creates new tokenizer object operating on json given as parameter.
   *
   * @param jsonAsText json text.
   */
  public JsonTokenizer(String jsonAsText) {
    this.jsonAsText = jsonAsText;
  }

  /**
   * Sets special flag that will point this tokenizer on
   * {@link pl.bristleback.server.bristle.serialization.system.converter.JsonTokenizer#nextToken()}
   * to return token that was previously read instead of taking next token. After <code>nextToken()</code> method
   * is invoked, the flag is set to false again (another <code>nextToken()</code> call will take the next token).
   */
  public void setNextReadRepeatedFromLast() {
    takeNextValueFromLastRead = true;
  }

  /**
   * Takes next json token. Last read token type can be retrieved using {@link JsonTokenizer#getLastTokenType()} method.
   * If read token is a value or property name, token value is stored and can be obtained using {@link JsonTokenizer#getLastTokenValue()} method.
   * If there is no more tokens, {@link JsonTokenType#END_OF_JSON} value is returned.
   *
   * @return type of last read token.
   */
  public JsonTokenType nextToken() {
    if (takeNextValueFromLastRead) {
      takeNextValueFromLastRead = false;
      return lastTokenType;
    }
    char c = this.nextClean();
    lastTokenBeginIndex = index;
    if (c == ':') {
      lastTokenType = JsonTokenType.PROPERTY_NAME;
      return nextToken();
    } else if (c == ',') {
      return nextToken();
    } else if (c == '{') {
      lastTokenType = JsonTokenType.OBJECT_START;
    } else if (c == '}') {
      lastTokenType = JsonTokenType.OBJECT_END;
    } else if (c == '[') {
      lastTokenType = JsonTokenType.ARRAY_START;
    } else if (c == ']') {
      lastTokenType = JsonTokenType.ARRAY_END;
    } else if (c == 0) {
      lastTokenType = JsonTokenType.END_OF_JSON;
    } else if (lastTokenType == JsonTokenType.PROPERTY_NAME) {
      lastTokenType = JsonTokenType.PROPERTY_VALUE;
      lastTokenValue = getNextRawValue();
    } else {
      lastTokenType = JsonTokenType.PROPERTY_NAME_OR_RAW_VALUE;
      lastTokenValue = getNextRawValue();
    }

    return lastTokenType;
  }

  private String getNextRawValue() {
    if (current() == '"') {
      return getNextRawStringValue();
    }
    return getNextRawNumberValue();
  }

  private String getNextRawStringValue() {
    StringBuilder builder = new StringBuilder();
    while (true) {
      char c = next();
      if (c == 0) {
        throw new DeserializationException("Json String must end with '\"' character");
      } else if (c == '"') {
        return builder.toString();
      } else if (c == '\\') {
        processEscapeCharacter(builder);
      } else {
        builder.append(c);
      }
    }
  }

  private void processEscapeCharacter(StringBuilder builder) {
    char c = next();
    switch (c) {
      case 'b':
        builder.append('\b');
        break;
      case 't':
        builder.append('\t');
        break;
      case 'n':
        builder.append('\n');
        break;
      case 'f':
        builder.append('\f');
        break;
      case 'r':
        builder.append('\r');
        break;
      case 'u':
        builder.append((char) Integer.parseInt(nextUnicodeNumber(), HEX_RADIX));
        break;
      case '"':
      case '\'':
      case '\\':
      case '/':
        builder.append(c);
        break;
      default:
        throw new DeserializationException("Illegal escape character: '" + c + "'");
    }
  }

  private String getNextRawNumberValue() {
    int firstIndex = index;
    char c = current();
    while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
      c = this.next();
    }
    if (index == firstIndex) {
      return null;
    }
    String numberAsString = jsonAsText.substring(firstIndex, index).trim();
    index--;  // we need to get back
    if (numberAsString.equalsIgnoreCase("null")) {
      return null;
    }

    return numberAsString;
  }

  /**
   * This method just reads entire value. Can be used for example if this value should be omitted.
   * All value types can be fast forwarded using this method (including arrays and objects).
   */
  public void fastForwardValue() {
    nextToken();
    if (lastTokenType == JsonTokenType.PROPERTY_NAME_OR_RAW_VALUE || lastTokenType == JsonTokenType.PROPERTY_VALUE) {
      return;
    } else if (lastTokenType == JsonTokenType.OBJECT_START) {
      fastForwardObject();
      return;
    } else if (lastTokenType == JsonTokenType.ARRAY_START) {
      fastForwardArray();
      return;
    }
    throw new DeserializationException("Corrupted json, value cannot start with type: " + lastTokenType);
  }

  private void fastForwardObject() {
    while (nextToken() == JsonTokenType.PROPERTY_NAME_OR_RAW_VALUE) {
      fastForwardValue();
    }
    if (lastTokenType != JsonTokenType.OBJECT_END) {
      throw new DeserializationException("Corrupted json, object must end with '}' character");
    }
  }

  private void fastForwardArray() {
    while (nextToken() != JsonTokenType.ARRAY_END) {
      if (lastTokenType == JsonTokenType.END_OF_JSON) {
        throw new DeserializationException("Corrupted json, array must end with ']' character");
      }
      setNextReadRepeatedFromLast();
      fastForwardValue();
    }
  }

  /**
   * Reads next value as a String, regardless of whether the value is raw or complex type.
   * For example, if value is of Object type {"property1":1}, this method will return {"property1":1}.
   * This method may be usable if returned String will be processed by other json deserialization tool.
   *
   * @return next value as String.
   */
  public String nextValueAsString() {
    int startIndex = index;
    nextToken();
    if (lastTokenType == JsonTokenType.PROPERTY_NAME_OR_RAW_VALUE || lastTokenType == JsonTokenType.PROPERTY_VALUE) {
      return lastTokenValue;
    }
    setNextReadRepeatedFromLast();
    fastForwardValue();
    return jsonAsText.substring(startIndex, index + 1);
  }

  private String nextUnicodeNumber() {
    if (jsonAsText.length() <= index + 4) {
      throw new DeserializationException("Cannot parse hexadecimal number, index out of bounds");
    }
    String numberAsString = jsonAsText.substring(index, index + 5);

    index += 4;
    return numberAsString;
  }

  /**
   * Get the next char in the string, skipping whitespace.
   *
   * @return A character, or 0 if there are no more characters.
   */
  private char nextClean() {
    while (true) {
      char c = next();
      if (c == 0 || c > ' ') {
        return c;
      }
    }
  }

  private char next() {
    index++;
    if (jsonAsText.length() <= index) {
      return 0;
    }

    return jsonAsText.charAt(index);
  }

  private char current() {
    return jsonAsText.charAt(index);
  }

  /**
   * Gets last read token value. It returns not null value only if last read token type is one of the following types:
   * <ul>
   * <li>{@link JsonTokenType#PROPERTY_NAME_OR_RAW_VALUE}</li>
   * <li>{@link JsonTokenType#PROPERTY_VALUE}</li>
   * </ul>
   *
   * @return last read token value.
   */
  public String getLastTokenValue() {
    return lastTokenValue;
  }

  /**
   * Gets last read token type.
   *
   * @return last read token type.
   */
  public JsonTokenType getLastTokenType() {
    return lastTokenType;
  }

  public int getIndex() {
    return index;
  }

  public int getLastTokenBeginIndex() {
    return lastTokenBeginIndex;
  }
}
