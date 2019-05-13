package main.java.com.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

class ClientHandler implements Runnable {
    private Scanner sc = new Scanner(System.in);
    private String name;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private Socket socket;
    private boolean isActive;

    public String getName() {
        return name;
    }

    boolean isActive() {
        return isActive;
    }

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        System.out.println("Constructor");
        this.dis = dis;
        this.dos = dos;
        try {
            this.name = dis.readUTF().split(":")[0];
        } catch (IOException io) {
            io.printStackTrace();
        }
        this.socket = s;
        this.isActive = true;
        System.out.println("Constructor ended for Client " + getName());
    }

    @Override
    public void run() {
        //setter(); //LANDMARK
        while (isActive) {
            try {
                String received = "";
                // receives the string
                received = dis.readUTF();
                System.out.println(received);

                // breaks the string into message and recipient part
                try {
                    StringTokenizer st = new StringTokenizer(received, "#");
                    String MsgToSend = "PRIVATE MESSAGE: " + st.nextToken();
                    String recipient = st.nextToken();

                    // searches for the recipient in the connected devices list.
                    // ar is the vector storing server of active users
                    for (ClientHandler item : main.java.com.chat.server.Server.clientList) {
                        // if avail recipient, to write on its stream
                        if (item.name.equals(recipient) && item.isActive) {
                            item.dos.writeUTF(MsgToSend);
                            break;
                        }
                    }
                } catch (Exception e) {
                    for (ClientHandler item : main.java.com.chat.server.Server.clientList) {
                        // if avail recipient, to write on its stream
                        if (item.isActive) {
                            item.dos.writeUTF(received);
                        }
                    }
                }
            } catch (IOException e) {
                isActive = false;
                e.printStackTrace();
                try {
                    this.dis.close();
                    this.dos.close();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }
            }
        }
    }
} 