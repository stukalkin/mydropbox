import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

class CommandMessage extends AbstractMessage {
    public enum Parametr {
        Info, File, Auth
    }
    private String filename;
    private String fileSecondname;
    private byte[] bytes;
    public Parametr param;

    public Parametr getParametr() {return param;}

    public String getFilename() {
        return filename;
    }

    public String getFileSecondname() {
        return fileSecondname;
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

    public CommandMessage (String login, String password) {
        param = Parametr.Auth;
        filename = login;
        fileSecondname = password;
    }
}
