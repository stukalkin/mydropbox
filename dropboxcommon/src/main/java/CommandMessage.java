import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

class CommandMessage extends AbstractMessage {
    public enum Parametr {
        Info, File
    }
    private String filename;
    private byte[] bytes;
    public Parametr param;

    public Parametr getParametr() {return param;}

    public String getFilename() {
        return filename;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public CommandMessage (Path path) throws IOException {
        param = Parametr.File;
        filename = path.getFileName().toString();
        bytes = Files.readAllBytes(path);
    }

    public CommandMessage (String string) {
        param = Parametr.Info;
        filename = string;
    }
}
