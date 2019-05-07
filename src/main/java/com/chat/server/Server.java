package main.java.com.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    // list to store the clients
    static List<ClientHandler> clientList = new ArrayList<>();
    // counter for clients
    static int clients = 0;

    public static int getClients() {
        return clients;
    }

    public static void setClients(int clients) {
        Server.clients = clients;
    }

    public static void main(String[] args) throws IOException {
        // server is listening on port 8888
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket socket;

        // running infinite loop
        while (true) {
            // accepts the incoming request
            socket = serverSocket.accept();

            //user out
            System.out.println("New server request received : " + socket);

            // obtains input and output streams
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Creating a new handler...");

            // creates a new handler
            ClientHandler next = new ClientHandler(socket, inputStream, outputStream);

            // creates a new Thread
            Thread t = new Thread(next);
            System.out.println("Adding " + next.getName() + " server to active server list");

            // adds this server to the list
            clientList.add(next);


            // starts the thread
            t.start();

            // increments for new server.
            setClients(clients++);


//            for (ClientHandler item : clientList) {
//                System.out.println(item);
//            }

        }
    }

//    public static String nextName() {
//        for (CLASS name : CLASS.values()) {
//            for (com.chat.server.ClientHandler item: clientList){
//                if (item.getName().equalsIgnoreCase(name.toString())) break;
//                else return name.toString();
//            }
//        }
//        return "DUMMY";
//    }
} 