package bhz.netty.http;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
 
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;
  
  
  public class HttpHelloWorldServerHandler extends ChannelHandlerAdapter {
      private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
  
      @Override
      public void channelReadComplete(ChannelHandlerContext ctx) {
          ctx.flush();
      }

      //响应请求方法
      @Override
      public void channelRead(ChannelHandlerContext ctx, Object msg) {
          if (msg instanceof HttpRequest) {
              HttpRequest req = (HttpRequest) msg;
                //是否发生服务器100连接错误
              if (HttpHeaderUtil.is100ContinueExpected(req)) {
                  ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
              }
              //是否存活
              boolean keepAlive = HttpHeaderUtil.isKeepAlive(req);
              FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));
              //设置响应文件类型
              response.headers().set(CONTENT_TYPE, "text/plain");
              //设置响应文件长度
              response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
                //没有存活就关闭通道
              if (!keepAlive) {
                  ctx.write(response).addListener(ChannelFutureListener.CLOSE);
              } else {
                  //存活的话就设置为保持连接
                  response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                  ctx.write(response);
              }
          }
      }
  
      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
          cause.printStackTrace();
          ctx.close();
      }
  }