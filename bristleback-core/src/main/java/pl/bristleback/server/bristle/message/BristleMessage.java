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

package pl.bristleback.server.bristle.message;

/**
 * Pawel Machowski
 * created at 18.05.12 20:21
 */
public class BristleMessage<T> {

  public static final String PAYLOAD_PROPERTY_NAME = "payload";

  private String id;
  private String name;
  private T payload;

  public BristleMessage<T> withId(String messageId) {
    this.id = messageId;
    return this;
  }

  public BristleMessage<T> withName(String messageName) {
    this.name = messageName;
    return this;
  }

  public BristleMessage<T> withPayload(T payloadContent) {
    this.payload = payloadContent;
    return this;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public T getPayload() {
    return payload;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }
}
