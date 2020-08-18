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
    public TextField txt_comment;
    final String clientRootPath = "./ClientDir";
    public ObjectEncoderOutputStream out;
    public ObjectDecoderInputStream in;
    public Socket socket;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream(), 1000 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download() throws IOException, ClassNotFoundException {  // скачивание с сервера
        String command = txt_fn.getText();
        if (!(command.equals(""))){
            CommandMessage cm = new CommandMessage(command);
            out.writeObject(cm);
        } else {txt_comment.setText("Enter file name to download");}
        CommandMessage cm = (CommandMessage) in.readObject();  // если приходит сообщение с файлом
        if (cm.getParametr() == CommandMessage.Parametr.File) {
            File file = new File(clientRootPath + "/" + cm.getFilename());
            if (!file.exists()) {
                Files.write(Paths.get(file.getPath()), cm.getBytes());
            } else txt_comment.setText("File exists");
        } else if (cm.getParametr() == CommandMessage.Parametr.Info) { // если приходит сообщение с текстом
            txt_comment.setText("Server answer: " + cm.getFilename());
        }
        update_lv("client");
    }

    public void upload() throws IOException { // загрузка на сервер
        String command = txt_fn.getText();
        if (!(command.equals(""))){
            CommandMessage cm = new CommandMessage(Paths.get(clientRootPath + "/" + command));
            out.writeObject(cm);
        } else {txt_comment.setText("Enter file name to upload");}
        update_lv("server");
    }

    public void disconnect() throws IOException {
        socket.getInputStream().close();
        socket.getOutputStream().close();
        socket.close();
    }

    public void connect() throws IOException, ClassNotFoundException {
        String login = txt_login.getText();
        String password = txt_password.getText();
        CommandMessage am = new CommandMessage(login, password);
        out.writeObject(am);
        CommandMessage cm = (CommandMessage) in.readObject();
        if (cm.getFilename().equals("Access granted")) {
            txt_comment.setText("Server answer: " + cm.getFilename());
            update_lv("client");
            update_lv("server");
        } else {txt_comment.setText("Server answer: " + cm.getFilename());}
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
