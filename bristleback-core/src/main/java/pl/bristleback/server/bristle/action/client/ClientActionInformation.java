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

package pl.bristleback.server.bristle.action.client;

import pl.bristleback.server.bristle.api.action.ClientActionSender;

import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Created on: 2012-05-27 08:38:39 <br/>
 *
 * @author Wojciech Niemiec
 */
public class ClientActionInformation {

  private String fullName;
  private String name;

  private List<ClientActionParameterInformation> parameters;
  private ClientActionSender sender;
  private int payloadLength;

  public ClientActionInformation(String name, String fullName,
                                 List<ClientActionParameterInformation> parameters, ClientActionSender sender) {
    this.name = name;
    this.fullName = fullName;
    this.parameters = parameters;
    this.sender = sender;

    payloadLength = checkPayloadLength();
  }

  private int checkPayloadLength() {
    int length = 0;
    for (ClientActionParameterInformation parameter : parameters) {
      if (parameter.isForSerialization()) {
        length++;
      }
    }
    return length;
  }

  public List<ClientActionParameterInformation> getParameters() {
    return parameters;
  }

  public String getName() {
    return name;
  }

  public String getFullName() {
    return fullName;
  }

  public ClientActionSender getResponse() {
    return sender;
  }

  public int getPayloadLength() {
    return payloadLength;
  }
}
