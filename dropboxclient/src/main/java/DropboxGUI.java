import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

public class DropboxGUI implements Initializable {
    public ListView<String> lv_client;
    public ListView<String> lv_server;
    public Button bnt_disconnect;
    public Button bnt_connect;
    public TextField txt_fn;
    public Button bnt_download;
    public Button bnt_upload;
    public TextField txt_login;
    public TextField txt_password;
    final String clientRootPath = "./ClientDir";
    public ObjectEncoderOutputStream out;
    public ObjectDecoderInputStream in;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Socket socket = new Socket("localhost", 8189);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream(), 1000 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        update_lv("client");
        update_lv("server");
    }

    public void download() throws IOException, ClassNotFoundException {  // скачивание с сервера
        String command = txt_fn.getText();
        if (!(command.equals(""))){
            RequestMessage rm = new RequestMessage(command);
            out.writeObject(rm);
        } else {System.out.println("Enter file name to download");}
        if (in.readObject() instanceof CommandMessage) {
            CommandMessage cm = (CommandMessage) in.readObject();
                    File file = new File(clientRootPath + "/" + cm.getFilename());
            if (!file.exists()) {
                Files.write(Paths.get(file.getPath()), cm.getBytes());
            } else System.out.println("File exists");
        } else if (in.readObject() instanceof infoMessage) {
            infoMessage im = (infoMessage) in.readObject();
            txt_fn.setText("Server answer: " + im.getMessage());
        }
        update_lv("client");
    }

    public void upload() throws IOException { // загрузка на сервер
        String command = txt_fn.getText();
        if (!(command.equals(""))){
            CommandMessage cm = new CommandMessage(Paths.get(clientRootPath + "/" + command));
            out.writeObject(cm);
        } else {System.out.println("Enter file name to upload");}
        update_lv("server");
    }

    public void disconnect() {
    }

    public void connect() {
    }

    public void update_lv (String clientOrServer) { //метод обновления полей файлов на стороне клиента и сервера
        if (clientOrServer.equals("client")) {
            File dir = new File(clientRootPath);
            lv_client.getItems().clear();
            for (String file : Objects.requireNonNull(dir.list())) {
                lv_client.getItems().add(file);
            }}
        else if (clientOrServer.equals("server")) {
            File dir = new File("./ServerDir");
            lv_server.getItems().clear();
            for (String file : Objects.requireNonNull(dir.list())) {
                lv_server.getItems().add(file);
            }
        }
    }
}
