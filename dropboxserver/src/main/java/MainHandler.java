import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.IOException;

public class MainHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        System.out.println("Client connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        System.out.println("Client disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        if (msg instanceof CommandMessage) { // запрос на создание файла на сервере
            File file = new File("./" + ((CommandMessage) msg).getFilename());
            file.createNewFile();
        } else if (msg instanceof RequestMessage) {
            // TODO: 05.08.2020  
        }
    }
}