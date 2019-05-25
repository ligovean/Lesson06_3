package client.sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller  implements Initializable {

    @FXML
    TextArea textArea;
    @FXML
    TextField textField;

    Socket socket;
    DataInputStream input;
    DataOutputStream output;

    final String IP_ADRESS = "localhost";
    final int PORT = 9999;
    //final int PORT = 8189;

    //Инициализация подключения к серверу
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            input = new DataInputStream(socket.getInputStream()); //Данные входящего потока с Сервера
            output = new DataOutputStream(socket.getOutputStream()); //Данные исходящего потока на Сервер

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String msg = null;
                    try {
                        while (true) {
                            msg = input.readUTF();
                            textArea.appendText(msg + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Отправка сообщения
    public void sendMsg() {
        if (!textField.getText().isEmpty()) {
            //textArea.appendText(textField.getText() + "\n");
            try {
                output.writeUTF(textField.getText());
                textField.clear();
                textField.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}