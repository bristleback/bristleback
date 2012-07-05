package pl.bristleback.server.bristle.engine.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.websocket.WebSocketFrame;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-09-25 14:17:02 <br/>
 *
 * @author Wojciech Niemiec
 */
public class HybiFrameEncoder extends OneToOneEncoder {
  private static Logger log = Logger.getLogger(HybiFrameEncoder.class.getName());

  private static final int FIRST_BYTE = 0x80;
  private static final int MIN_DATA_LENGTH_SECOND_TIER = 126;
  private static final int MIN_DATA_LENGTH_THIRD_TIER = 65535;

  @Override
  protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
    if (msg instanceof WebSocketFrame) {
      WebSocketFrame frame = (WebSocketFrame) msg;
      ChannelBuffer data = frame.getBinaryData();

      int firstByte = prepareFirstByte(frame);
      ChannelBuffer encoded = writeFundamentalData(data, channel, firstByte);
      encoded.writeBytes(data, data.readerIndex(), data.readableBytes());
      return encoded;
    }
    return msg;
  }

  private ChannelBuffer writeFundamentalData(ChannelBuffer data, Channel channel, int firstByte) {
    ChannelBuffer encoded;
    int dataLength = data.readableBytes();
    if (dataLength < MIN_DATA_LENGTH_SECOND_TIER) {
      encoded = channel.getConfig().getBufferFactory().getBuffer(data.order(), data.readableBytes() + 2);
      encoded.writeByte(firstByte);
      encoded.writeByte(dataLength);
    } else if (dataLength < MIN_DATA_LENGTH_THIRD_TIER) {
      encoded = channel.getConfig().getBufferFactory().getBuffer(data.order(), data.readableBytes() + 4);
      encoded.writeByte(firstByte);
      encoded.writeByte(MIN_DATA_LENGTH_SECOND_TIER);
      encoded.writeShort(dataLength);
    } else {
      encoded = channel.getConfig().getBufferFactory().getBuffer(data.order(), data.readableBytes() + 10);
      encoded.writeByte(firstByte);
      encoded.writeByte(MIN_DATA_LENGTH_THIRD_TIER);
      encoded.writeLong(dataLength);
    }
    return encoded;
  }


  private int prepareFirstByte(WebSocketFrame frame) {
    return FIRST_BYTE | frame.getType();
  }
}