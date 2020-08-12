import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws IOException {
        File file = new File("./ServerDir");
        if (!file.exists()) {
            file.mkdir();
        }System.out.println("Client connected");
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
            File file = new File("./ServerDir/" + ((CommandMessage) msg).getFilename());
            CommandMessage cm = (CommandMessage) msg;
            if (!file.exists()) {
                Files.write(Paths.get(file.getPath()), cm.getBytes());
            } else System.out.println("File exists");
        } else if (msg instanceof RequestMessage) {
            // TODO: 05.08.2020  
        }
    }
}