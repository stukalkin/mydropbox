import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommandMessage implements Serializable {
    private String filename;
    private byte[] bytes;

    public String getFilename() {
        return filename;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public CommandMessage (Path path) throws IOException {
        filename = path.getFileName().toString();
        bytes = Files.readAllBytes(path);
    }
}
