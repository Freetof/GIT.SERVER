package main.java.com.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

class ClientHandler implements Runnable {
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
        System.out.println("Constructor"); //system tag
        this.dis = dis;
        this.dos = dos;
        try {
            this.name = dis.readUTF().split(":")[0]; //string split using regex
        } catch (IOException io) {
            io.printStackTrace();
        }
        this.socket = s;
        this.isActive = true;
        System.out.println("Constructor ended for Client " + getName()); //system tag
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                String received = "";
                // receives the string
                received = dis.readUTF();
                System.out.println(received);

                // breaks the string into message and recipient part
                try {
                    StringTokenizer st = new StringTokenizer(received, "#");
                    //tags "private"
                    String MsgToSend = "PRIVATE MESSAGE: " + st.nextToken();
                    String recipient = st.nextToken();

                    // searches for the recipient in the connected devices list
                    for (ClientHandler item : main.java.com.chat.server.Server.clientList) {
                        // if avail recipient, to write on its stream
                        if (item.name.equals(recipient) && item.isActive) {
                            item.dos.writeUTF(MsgToSend);
                            break;
                        }
                    }
                } catch (Exception e) {
                    for (ClientHandler item : main.java.com.chat.server.Server.clientList) {
                        // broadcast
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