import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler extends ChannelInboundHandlerAdapter {
    final String serverRootPath = "./ServerDir";

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws IOException {
        File file = new File(serverRootPath);
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
            File file = new File(serverRootPath + "/" + ((CommandMessage) msg).getFilename());
            CommandMessage cm = (CommandMessage) msg;
            if (!file.exists()) {
                Files.write(Paths.get(file.getPath()), cm.getBytes());
            } else System.out.println("File exists");
        } else if (msg instanceof RequestMessage) { // запрос на отправку файла
            RequestMessage rm = (RequestMessage) msg;
            File file = new File(serverRootPath + "/" + rm.getFilename());
            if (!file.exists()) {
                infoMessage im = new infoMessage("File not exist");
                ctx.writeAndFlush(im);
            } else {
                CommandMessage cm = new CommandMessage(Paths.get(file.getPath()));
                ctx.writeAndFlush(cm);
            }
        }
    }
}