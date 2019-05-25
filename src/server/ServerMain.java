package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerMain {
    private Vector<ClientHandler> clientPool = new Vector();

    ServerSocket serverSocket = null;
    Socket socket = null;

    public ServerMain() {

        {
            try {
                serverSocket = new ServerSocket(9999);
                Integer clientInd = 0;
                System.out.println("Сервер запущен...");

                while (true){
                    socket = serverSocket.accept();
                    ++clientInd;
                    //System.out.println("Клиент " + clientInd + " подключился. RSA: " + socket.getRemoteSocketAddress());
                    clientPool.add(new ClientHandler(socket,this, "Client " + clientInd));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public void dropClientEntyty(ClientHandler client){
        clientPool.remove(client);
    }

    public void broadcastMsg(String msgAll) {
        for (ClientHandler clients:clientPool) {
            clients.sendMsg(msgAll);
        }
    }
}
