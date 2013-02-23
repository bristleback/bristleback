package pl.bristleback.server.bristle.api;

import pl.bristleback.server.bristle.engine.base.ConnectedUser;

/**
 * Data controller processes incoming text and binary data.
 * Each non control message sent by user is forwarded to data controller implementation.
 * There might be multiple active data controllers, from which connecting clients choose one they wish to communicate with.
 * <p/>
 * Created on: 2011-07-19 15:18:15 <br/>
 *
 * @author Wojciech Niemiec
 * @see pl.bristleback.server.bristle.action.ActionController Action Data Controller
 */
public interface DataController extends ConfigurationAware {

  /**
   * Processes incoming, serialized text data, sent by given user.
   *
   * @param textData text data.
   * @param user     user that sent the message.
   */
  void processTextData(String textData, ConnectedUser user);

  /**
   * Processes incoming, serialized binary data, sent by given user.
   *
   * @param binaryData binary data.
   * @param user       user that sent the message.
   */
  void processBinaryData(byte[] binaryData, ConnectedUser user);
} 