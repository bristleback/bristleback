package pl.bristleback.server.bristle.serialization.system.json.extractor;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.BristlebackConfig;
import pl.bristleback.server.bristle.serialization.system.PropertySerialization;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-11-24 17:15:39 <br/>
 *
 * @author Wojciech Niemiec
 */
@Component
public class DateValueSerializer implements ValueSerializer<Date> {

  public static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";

  private DateFormat defaultDateFormat;

  @Override
  public void init(BristlebackConfig configuration) {
    //todo - init default format using configuration
    defaultDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
  }

  @Override
  public Date toValue(String valueAsString, PropertySerialization information) throws Exception {
    //todo - use non default formatting possible
    return defaultDateFormat.parse(valueAsString);
  }

  @Override
  public String toText(Date value, PropertySerialization information) {
    //todo - use non default formatting possible
    return JSONObject.quote(defaultDateFormat.format(value));
  }
}
