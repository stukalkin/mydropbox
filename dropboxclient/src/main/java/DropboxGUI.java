import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File dir = new File(clientRootPath);
        for (String file : dir.list()) {
            lv_client.getItems().add(file);
        }
    }
}
