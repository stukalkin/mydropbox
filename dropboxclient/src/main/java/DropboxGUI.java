import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class DropboxGUI implements Initializable {
    public ListView lv_client;
    public ListView lv_server;
    public Button bnt_disconnect;
    public Button bnt_connect;
    public TextField txt_fn;
    public Button bnt_download;
    public Button bnt_upload;
    public TextField txt_login;
    public TextField txt_password;
    final String clientRootPath = ".";
    private Socket socket;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
            ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream(), 1000 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File dir = new File(clientRootPath);
        for (String file : dir.list()) {
            lv_client.getItems().add(file);
        }
    }

    public void download(ActionEvent actionEvent) {
        String command = txt_fn.getText();
    }

    public void upload(ActionEvent actionEvent) {
        String command = txt_fn.getText();
    }
}
