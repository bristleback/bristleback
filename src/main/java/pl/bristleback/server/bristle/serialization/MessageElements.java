package pl.bristleback.server.bristle.serialization;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-03-11 16:36:51 <br/>
 *
 * @author Wojciech Niemiec
 */
public enum MessageElements {

  ACTION_CLASS("cl"),
  ACTION("act"),
  ID("id"),
  CONTENT("cnt"),
  EXCEPTION("exc");

  MessageElements(String value) {
    this.value = value;
    this.quotedValue = "\"" + value + "\"";
  }

  private String value;
  private String quotedValue;

  public String getValue() {
    return value;
  }

  public String getQuotedValue() {
    return quotedValue;
  }
}
