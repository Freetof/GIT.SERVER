package main.java.com.chat.server;

import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
    // list to store the clients
    static List<ClientHandler> clientList = new ArrayList<>();

    public Server() {
        System.out.println("Server has been started");
        try {
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
                ClientHandler clientHandler = new ClientHandler(socket, inputStream, outputStream);

                // creates a new Thread
                Thread thread = new Thread(clientHandler);
                System.out.println("Adding " + clientHandler.getName() + " clientHandler to active server list");

                // adds this server to the list
                clientList.add(clientHandler);


                // starts the thread
                thread.start();

                // increments for new server.

                System.out.println("Current list of clients:");
                for (ClientHandler item : clientList) {
                    System.out.println(item.getName() + "\t: " + (item.isActive() ? "Active" : "Inactive"));
                }
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        new Server();
    }

}