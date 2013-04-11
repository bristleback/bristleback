package pl.bristleback.server.mock.beans;

import org.apache.log4j.Logger;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-01-10 19:58:22 <br/>
 *
 * @author Wojciech Niemiec
 */
public class VerySimpleMockBean {

  private static Logger log = Logger.getLogger(VerySimpleMockBean.class.getName());

  private int simpleField;

  public int getSimpleField() {
    return simpleField;
  }

  public void setSimpleField(int simpleField) {
    this.simpleField = simpleField;
  }
}
