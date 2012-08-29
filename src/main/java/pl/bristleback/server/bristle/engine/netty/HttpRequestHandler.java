package pl.bristleback.server.bristle.engine.netty;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.websocketx.WebSocket00FrameDecoder;
import org.jboss.netty.handler.codec.http.websocketx.WebSocket00FrameEncoder;
import org.jboss.netty.handler.codec.http.websocketx.WebSocket08FrameDecoder;
import org.jboss.netty.handler.codec.http.websocketx.WebSocket08FrameEncoder;
import org.jboss.netty.handler.codec.http.websocketx.WebSocket13FrameDecoder;
import org.jboss.netty.handler.codec.http.websocketx.WebSocket13FrameEncoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.util.CharsetUtil;
import pl.bristleback.server.bristle.api.DataController;
import pl.bristleback.server.bristle.api.WebsocketConnector;
import pl.bristleback.server.bristle.engine.WebsocketVersions;
import pl.bristleback.server.bristle.utils.ExtendedHttpHeaders;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.setContentLength;

/**
 * //@todo class description
 * <p/>
 * Created on: 2011-07-18 13:52:09 <br/>
 *
 * @author Wojciech Niemiec
 * @author Andrea Nanni
 */
public class HttpRequestHandler {
  private static Logger log = Logger.getLogger(HttpRequestHandler.class.getName());

  private static final String WEBSOCKET_ACCEPT_HYBI_10_PARAMETER = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
  private static final int HIXIE_BUFFER_SIZE = 16;

  private static final String WEBSOCKET_PATH = "/websocket";
  private static final String NULL_VALUE = "null";

  private NettyServerEngine engine;

  public HttpRequestHandler(NettyServerEngine engine) {
    this.engine = engine;
  }

  public void handleHttpRequest(ChannelHandlerContext context, HttpRequest request) {
    // request must be valid http GET request
    if (isNotHttpGetRequest(request)) {
      sendHttpResponse(context, request, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
      return;
    }
    try {
      HttpResponse response = processHandshake(context, request);
      if (!response.getStatus().equals(HttpResponseStatus.SWITCHING_PROTOCOLS)) {
        // sending error http response
        sendHttpResponse(context, request, response);
        return;
      }
      initializeWebsocketConnector(context, request, response);
      replaceListeners(context, request);
    } catch (NoSuchAlgorithmException e) {
      context.getChannel().close();
    }

  }

  @SuppressWarnings("rawtypes")
  private void replaceListeners(ChannelHandlerContext context, HttpRequest request) {
    int maxFrameSize = engine.getEngineConfiguration().getMaxFrameSize();
    boolean maskPayload = true;

    WebsocketConnector connector = (WebsocketConnector) context.getAttachment();
    String wsVersion = connector.getWebsocketVersion();

    ReplayingDecoder decoder = null;
    OneToOneEncoder encoder = null;
    if (wsVersion.equals(WebsocketVersions.HYBI_13.getVersionCode())) {
      decoder = new WebSocket13FrameDecoder(maskPayload, true, maxFrameSize);
      encoder = new WebSocket13FrameEncoder(maskPayload);
    } else if (wsVersion.equals(WebsocketVersions.HYBI_10.getVersionCode())) {
      decoder = new WebSocket08FrameDecoder(maskPayload, true, maxFrameSize);
      encoder = new WebSocket08FrameEncoder(maskPayload);
    } else if (wsVersion.equals(WebsocketVersions.HIXIE_76.getVersionCode())) {
      decoder = new WebSocket00FrameDecoder((long) maxFrameSize);
      encoder = new WebSocket00FrameEncoder();
    }

    ChannelPipeline pipeline = context.getChannel().getPipeline();
    pipeline.replace("decoder", "wsDecoder", decoder);
    pipeline.replace("encoder", "wsEncoder", encoder);
    pipeline.remove("aggregator");

  }

  private void initializeWebsocketConnector(final ChannelHandlerContext context, final HttpRequest request, HttpResponse response) {
    String controllerName = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL);
    final DataController controllerUsed = engine.getConfiguration().getDataController(controllerName);
    ChannelFuture future = context.getChannel().write(response);
    future.addListener(new ChannelFutureListener() {

      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          WebsocketConnector<Channel> connector = new NettyConnector(future.getChannel(), engine, controllerUsed);
          connector.setWebsocketVersion(request.getHeader(ExtendedHttpHeaders.Names.SEC_WEBSOCKET_VERSION));
          if (future.getChannel().isConnected()) {
            context.setAttachment(connector);
            engine.onConnectionOpened(connector);
          }
        }
      }
    });
  }

  private HttpResponse processHandshake(ChannelHandlerContext context, HttpRequest request) throws NoSuchAlgorithmException {
    HttpResponse response;
    // HttpHeaders.Names.WEBSOCKET_ORIGIN; HttpHeaders.Names.WEBSOCKET_LOCATION not checked because of lack of compatibility on client side
    if (!hasHeaderWithValue(request, HttpHeaders.Names.CONNECTION, HttpHeaders.Values.UPGRADE)
      || !hasHeaderWithValue(request, HttpHeaders.Names.UPGRADE, HttpHeaders.Values.WEBSOCKET)) {
      // bad handshake, error message will be sent connection will be closed
      return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
    }
    if (hasBadProtocolHeader(request)) {
      // bad protocol, send special error code saying that server does not agree to any of the client's requested sub protocols
      return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_ACCEPTABLE);
    }

    if (request.containsHeader(ExtendedHttpHeaders.Names.SEC_WEBSOCKET_VERSION)) {
      // treat this request as Hybi draft 4+ handshake (validated according to Websocket Hybi draft 10 version)
      response = processHybiHandshake(request);
    } else {
      // treat this request as Hixie-76/Hybi-0 handshake (validated according to Websocket Hixie-76 version)
      response = processHixieHandshake(request);
    }
    return response;
  }

  private HttpResponse processHixieHandshake(HttpRequest request) throws NoSuchAlgorithmException {
    HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS);
    addStandardResponseHeaders(request, response);
    response.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_ORIGIN, request.getHeader(HttpHeaders.Names.ORIGIN));

    ChannelBuffer responseContent = createHixieHandshakeContent(request);
    response.setContent(responseContent);
    return response;
  }

  private HttpResponse processHybiHandshake(HttpRequest request) throws NoSuchAlgorithmException {

    String websocketKey = request.getHeader(ExtendedHttpHeaders.Names.SEC_WEBSOCKET_KEY);
    if (StringUtils.isEmpty(websocketKey)) {
      return new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
    }

    HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS);
    addStandardResponseHeaders(request, response);

    String websocketAcceptValue = computeHybiAcceptValue(websocketKey);
    response.addHeader(ExtendedHttpHeaders.Names.SEC_WEBSOCKET_ACCEPT, websocketAcceptValue);

    return response;
  }

  private String computeHybiAcceptValue(String websocketKey)
    throws NoSuchAlgorithmException {
    String concatenatedKey = websocketKey + WEBSOCKET_ACCEPT_HYBI_10_PARAMETER;
    byte[] result = MessageDigest.getInstance("SHA").digest(concatenatedKey.getBytes());
    result = Base64.encodeBase64(result);
    return new String(result);
  }

  private void addStandardResponseHeaders(HttpRequest request, HttpResponse response) {
    response.addHeader(HttpHeaders.Names.UPGRADE, HttpHeaders.Values.WEBSOCKET);
    response.addHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.UPGRADE);
    response.addHeader(HttpHeaders.Names.LOCATION, HttpHeaders.Values.UPGRADE);
    response.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_LOCATION, getHttpClientLocation(request));

    String protocolRequested = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL);
    if (StringUtils.isNotEmpty(protocolRequested)) {
      response.addHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL, protocolRequested);
    }
  }

  private boolean hasBadProtocolHeader(HttpRequest request) {
    String protocol = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_PROTOCOL);
    if (protocol == null) {
      // no protocol sent by client, by default, first protocol listed in configuration will be used
      return false;
    }
    if (protocol.isEmpty()) {
      // empty value of websocket protocol header, violation of hixie and hybi protocols
      return true;
    }
    return !engine.getConfiguration().getDataControllers().hasController(protocol);
  }

  private ChannelBuffer createHixieHandshakeContent(HttpRequest request) throws NoSuchAlgorithmException {
    String key1 = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY1);
    String key2 = request.getHeader(HttpHeaders.Names.SEC_WEBSOCKET_KEY2);
    int keyA = (int) (Long.parseLong(key1.replaceAll("[^0-9]", "")) / key1.replaceAll("[^ ]", "").length());
    int keyB = (int) (Long.parseLong(key2.replaceAll("[^0-9]", "")) / key2.replaceAll("[^ ]", "").length());
    long keyC = request.getContent().readLong();

    ChannelBuffer responseBuffer = ChannelBuffers.buffer(HIXIE_BUFFER_SIZE);
    responseBuffer.writeInt(keyA);
    responseBuffer.writeInt(keyB);
    responseBuffer.writeLong(keyC);

    return ChannelBuffers.wrappedBuffer(MessageDigest.getInstance("MD5").digest(responseBuffer.array()));
  }

  private String getHttpClientLocation(HttpRequest request) {
    String location = request.getHeader(HttpHeaders.Names.ORIGIN);
    if (StringUtils.isEmpty(location) || NULL_VALUE.equals(location)) {
      return "ws://" + request.getHeader(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
    }
    return location;
  }

  private boolean hasHeaderWithValue(HttpRequest request, String headerName, String expectedHeaderValue) {
    String headerValue = request.getHeader(headerName);
    return (headerValue != null) && (expectedHeaderValue.equalsIgnoreCase(headerValue));
  }

  private boolean isNotHttpGetRequest(HttpRequest aReq) {
    return aReq.getMethod() != HttpMethod.GET;
  }

  private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
    // Generate an error page if response status code is not OK (200).
    if (res.getStatus().getCode() != HttpResponseStatus.OK.getCode()) {
      res.setContent(
        ChannelBuffers.copiedBuffer(
          res.getStatus().toString(), CharsetUtil.UTF_8));
      setContentLength(res, res.getContent().readableBytes());
    }

    // Send the response and close the connection if necessary.
    ChannelFuture f = ctx.getChannel().write(res);
    if (!isKeepAlive(req) || res.getStatus().getCode() != HttpResponseStatus.OK.getCode()) {
      f.addListener(ChannelFutureListener.CLOSE);
    }
  }
}