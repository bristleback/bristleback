package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.api.users.IdentifiedUser;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-19 15:18:15 <br/>
 *
 * @author Wojciech Niemiec
 */
public interface DataController {

  void init(BristlebackConfig configuration);

  void processTextData(String textData, IdentifiedUser user);

  void processBinaryData(byte[] binaryData, IdentifiedUser user);
} 