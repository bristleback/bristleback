package pl.bristleback.server.bristle.engine.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.OperationCodes;
import pl.bristleback.server.bristle.engine.controller.DefaultFrontController;

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
public class WebsocketFrameHandler {
  private static Logger log = Logger.getLogger(WebsocketFrameHandler.class.getName());

  @Inject
  @Named("defaultFrontController")
  private FrontController frontController;

  public WebsocketFrameHandler() {
    frontController = new DefaultFrontController();
  }

  @SuppressWarnings("rawtypes")
  public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
    WebsocketConnector connector = (WebsocketConnector) ctx.getAttachment();
    int operationCode = 0;

    if (frame instanceof TextWebSocketFrame) {
      operationCode = OperationCodes.TEXT_FRAME_CODE.getCode();
    } else if (frame instanceof BinaryWebSocketFrame) {
      operationCode = OperationCodes.BINARY_FRAME_CODE.getCode();
    } else if (frame instanceof CloseWebSocketFrame) {
      operationCode = OperationCodes.CLOSE_FRAME_CODE.getCode();
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
