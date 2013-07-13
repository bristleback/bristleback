package pl.bristleback.server.bristle.serialization;

import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.message.BristleMessage;
import pl.bristleback.server.bristle.serialization.system.DeserializationException;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenType;
import pl.bristleback.server.bristle.serialization.system.json.converter.JsonTokenizer;

import java.util.ArrayList;
import java.util.List;

@Component
public class RawMessageSerializationEngine {

  public BristleMessage<String[]> deserialize(String serializedMessage) {
    JsonTokenizer tokenizer = new JsonTokenizer(serializedMessage);
    JsonTokenType tokenType = tokenizer.nextToken();
    if (tokenType != JsonTokenType.OBJECT_START) {
      throw new DeserializationException("Serialized json object must start with '{' character");
    }
    BristleMessage<String[]> message = new BristleMessage<String[]>();
    while (tokenizer.nextToken() == JsonTokenType.PROPERTY_NAME_OR_RAW_VALUE) {
      String propertyName = tokenizer.getLastTokenValue();
      if (propertyName.equals("id")) {
        message.withId(tokenizer.nextValueAsString());
      } else if (propertyName.equals("name")) {
        message.withName(tokenizer.nextValueAsString());
      } else if (propertyName.equals("payload")) {
        processPayload(serializedMessage, tokenizer, message);
      }
    }
    return message;
  }

  private void processPayload(String serializedMessage, JsonTokenizer tokenizer, BristleMessage<String[]> message) {
    JsonTokenType tokenType;
    tokenType = tokenizer.nextToken();
    if (tokenType != JsonTokenType.ARRAY_START) {
      throw new DeserializationException("Serialized payload object must start with '[' character");
    }
    List<String> payload = new ArrayList<String>();
    while (tokenizer.nextToken() != JsonTokenType.ARRAY_END) {
      if (tokenizer.getLastTokenType() == JsonTokenType.END_OF_JSON) {
        throw new DeserializationException("Serialized json array must end with ']' character");
      }
      int beginIndex = tokenizer.getLastTokenBeginIndex();
      tokenizer.setNextReadRepeatedFromLast();
      tokenizer.fastForwardValue();
      String payloadPart = serializedMessage.substring(beginIndex, tokenizer.getIndex() + 1);
      payload.add(payloadPart);
    }
    message.withPayload(payload.toArray(new String[payload.size()]));
  }

}