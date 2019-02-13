
  package bhz.netty.http;
  
  import io.netty.channel.ChannelInitializer;
  import io.netty.channel.ChannelPipeline;
  import io.netty.channel.socket.SocketChannel;
  import io.netty.handler.codec.http.HttpServerCodec;
  import io.netty.handler.ssl.SslContext;
  
  public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {
  
      private final SslContext sslCtx;

      //ssl安全协议
      public HttpHelloWorldServerInitializer(SslContext sslCtx) {
         this.sslCtx = sslCtx;
      }

      //初始化通道方法
      @Override
      public void initChannel(SocketChannel ch) {
          ChannelPipeline p = ch.pipeline();
          if (sslCtx != null) {
              p.addLast(sslCtx.newHandler(ch.alloc()));
          }
          p.addLast(new HttpServerCodec());
          p.addLast(new HttpHelloWorldServerHandler());
      }
  }