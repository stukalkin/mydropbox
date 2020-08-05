import java.io.Serializable;

public class RequestMessage implements Serializable {
    private String filename;

    public String getFilename() {
        return filename;
    }

    public RequestMessage (String filename) {
        this.filename = filename;
    }
}
