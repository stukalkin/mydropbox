import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler extends ChannelInboundHandlerAdapter {
    final String serverRootPath = "./ServerDir";

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext){
        File file = new File(serverRootPath);
        if (!file.exists()) {
            file.mkdir();
        }System.out.println("Connection established");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        System.out.println("Disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        CommandMessage cm = (CommandMessage) msg; // прилетело сообщение
        if (cm.getParametr() == CommandMessage.Parametr.File) { // если прилетел файл
            File file = new File(serverRootPath + "/" + cm.getFilename()); //создаем файл
            if (!file.exists()) { //если такой не существует
                Files.write(Paths.get(file.getPath()), cm.getBytes()); // то создаем файл и в него кидаем байты
            } else System.out.println("File exists"); // если файл есть, то выйдет сообщение
        } else if (cm.getParametr() == CommandMessage.Parametr.Info) { // но если прилетело инфо
            File file = new File(serverRootPath + "/" + cm.getFilename()); // создаем файл с новым путем
            if (!file.exists()) { // если не существует такой файл
                CommandMessage сmOut = new CommandMessage("File not exist"); //то посылаем сообщение клиенту
                ctx.writeAndFlush(сmOut); // вот тут посылаем
            } else { //ну а если есть
                CommandMessage сmOut = new CommandMessage(Paths.get(file.getPath())); //то посылаем байты
                ctx.writeAndFlush(сmOut);
            }
        }
    }
}