import java.io.Serializable;

public class infoMessage implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public infoMessage (String message) {
        this.message = message;
    }
}
