package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ClientHandler {
    private String clientName;
    private Socket socket;
    private ServerMain server;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, ServerMain server, String name) {
            this.socket = socket;
            this.server = server;
            this.clientName = name;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    input = new DataInputStream(socket.getInputStream());
                    output = new DataOutputStream(socket.getOutputStream());

                    server.broadcastMsg("======/" + name + " вошел в чат!/======");
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " ======/" + name + " вошел в чат!/======");
                    while (true) {
                        String msg = input.readUTF();
                        System.out.println(msg);
                        //Остановка клиента по стопслову /end
                        if(msg.equals("/end")) {
                            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " ======/" + name + " покинул чат!/======");
                            dropClientEntyty();
                            //Трансляция сообщения о том, что клиент вышел из чата
                            server.broadcastMsg("======/" + name + " покинул чат!/======");
                            socket.close();
                            break;
                        }
                        //Трансляция сообщения во все клиенты
                        server.broadcastMsg(name + ": " + msg);
                    }
                } catch (IOException e) {
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " ======/" + name + " покинул чат!/======");
                    dropClientEntyty();
                    //Трансляция сообщения о том, что клиент вышел из чата
                    server.broadcastMsg("======/" + name + " покинул чат!/======");
                    //e.printStackTrace();
                }finally {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void sendMsg(String msg) {
        try {
            output.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dropClientEntyty(){
        server.dropClientEntyty(this);
    }
}