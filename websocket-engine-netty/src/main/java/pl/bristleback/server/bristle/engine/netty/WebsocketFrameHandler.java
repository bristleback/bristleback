package pl.bristleback.server.bristle.engine.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.springframework.stereotype.Component;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.OperationCode;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-19 13:15:02 <br/>
 *
 * @author Wojciech Niemiec
 * @author Andrea Nanni
 */
@Component
public class WebsocketFrameHandler {

  @Inject
  @Named("defaultFrontController")
  private FrontController frontController;

  @SuppressWarnings("rawtypes")
  public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
    WebsocketConnector connector = (WebsocketConnector) ctx.getAttachment();
    int operationCode = 0;

    if (frame instanceof TextWebSocketFrame) {
      operationCode = OperationCode.TEXT_FRAME_CODE.getCode();
    } else if (frame instanceof BinaryWebSocketFrame) {
      operationCode = OperationCode.BINARY_FRAME_CODE.getCode();
    } else if (frame instanceof CloseWebSocketFrame) {
      operationCode = OperationCode.CLOSE_FRAME_CODE.getCode();
    }
    frontController.processCommand(connector, operationCode, getData(frame));
  }

  private Object getData(WebSocketFrame frame) {
    if (frame instanceof TextWebSocketFrame) {
      return ((TextWebSocketFrame) frame).getText();
    } else {
      return frame.getBinaryData();
    }
  }
}
