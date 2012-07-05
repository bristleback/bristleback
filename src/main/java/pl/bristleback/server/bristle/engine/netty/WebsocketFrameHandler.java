package pl.bristleback.server.bristle.engine.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.websocket.WebSocketFrame;
import pl.bristleback.server.bristle.api.FrontController;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.controller.DefaultFrontController;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-19 13:15:02 <br/>
 *
 * @author Wojciech Niemiec
 */
public class WebsocketFrameHandler {
  private static Logger log = Logger.getLogger(WebsocketFrameHandler.class.getName());

  @Inject
  @Named("defaultFrontController")
  private FrontController frontController;

  public WebsocketFrameHandler() {
    frontController = new DefaultFrontController();
  }

  public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
    WebsocketConnector connector = (WebsocketConnector) ctx.getAttachment();
    frontController.processCommand(connector, frame.getType(), getData(frame));
  }

  private Object getData(WebSocketFrame frame) {
    if (frame.isText()) {
      return frame.getTextData();
    } else {
      return frame.getBinaryData();
    }
  }
}
