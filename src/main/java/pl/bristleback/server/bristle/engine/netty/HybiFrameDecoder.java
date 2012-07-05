package pl.bristleback.server.bristle.engine.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.websocket.DefaultWebSocketFrame;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;
import pl.bristleback.server.bristle.engine.OperationCodes;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-24 16:55:55 <br/>
 *
 * @author Wojciech Niemiec
 */
public class HybiFrameDecoder extends ReplayingDecoder<VoidEnum> {
  private static Logger log = Logger.getLogger(HybiFrameDecoder.class.getName());

  private static final int MASKING_ENABLED = 1;

  private static final int OPERATION_CODE_MASK = 0x0F;
  private static final int FRAME_SIZE_MASK = 0x7F;

  private static final int FRAME_SIZE_AS_SHORT_VALUE_KEY = 126;
  private static final int FRAME_SIZE_AS_LONG_VALUE_KEY = 127;

  private static final Object NO_MESSAGE_RECEIVED = null;

  private int maxFrameSize;
  private boolean receivedClosingHandshake;

  public HybiFrameDecoder(int maxFrameSize) {
    this.maxFrameSize = maxFrameSize;
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, VoidEnum state) throws Exception {
// Discard all data received if closing handshake was received before.
    if (receivedClosingHandshake) {
      buffer.skipBytes(actualReadableBytes());
      return NO_MESSAGE_RECEIVED;
    }


    byte data = buffer.readByte();
    int fin = (data & (1 << 7)) >> 7;
    int rsv1 = (data & (1 << 6)) >> 6;
    int rsv2 = (data & (1 << 5)) >> 5;
    int rsv3 = (data & (1 << 4)) >> 4;

    int operation = data & OPERATION_CODE_MASK;

    data = buffer.readByte();
    int maskEnabled = (data & (1 << 7)) >> 7;
    if (maskEnabled != MASKING_ENABLED) {
      buffer.skipBytes(actualReadableBytes());
      return protocolErrorFrame();
    }

    long frameSize = computeFrameSize(buffer, data);

    if (frameSize > maxFrameSize) {
      return protocolErrorFrame();
    }

    byte[] mask = new byte[4];
    buffer.readBytes(mask);

    byte[] payload = new byte[(int) frameSize];
    buffer.readBytes(payload);
    for (int i = 0; i < payload.length; i++) {
      payload[i] = (byte) (payload[i] ^ mask[i % 4]);

    }
    if (operation == OperationCodes.CLOSE_FRAME_CODE.getCode()) {
      receivedClosingHandshake = true;
    }

    return new DefaultWebSocketFrame(operation, ChannelBuffers.copiedBuffer(payload));
  }

  private long computeFrameSize(ChannelBuffer buffer, byte data) {
    long frameSize = data & FRAME_SIZE_MASK;

    if (frameSize == FRAME_SIZE_AS_SHORT_VALUE_KEY) {
      frameSize = buffer.readUnsignedShort();
    } else if (frameSize == FRAME_SIZE_AS_LONG_VALUE_KEY) {
      frameSize = buffer.readLong() << 1 >> 1;
    }
    return frameSize;
  }

  private Object protocolErrorFrame() {
    return null;
  }
}