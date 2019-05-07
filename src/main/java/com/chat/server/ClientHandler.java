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
    //private String clientName;
    private Socket s;
    private boolean isloggedin;

    public String getName() {
        return name;
    }

    //private String[] parts;

//    private void getRealName(@NotNull DataInputStream data) {
//        try {
//            parts = data.readUTF().split(":");
//            System.out.println(parts[1].trim());
//            clientName = parts[0].trim();
//        } catch (IOException io) {
//            io.printStackTrace();
//        }
//    }

//    public String getClientName() {
//        return clientName;
//    }

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        System.out.println("Constructor");
        this.dis = dis;
        this.dos = dos;
        try {
            this.name = dis.readUTF().split(":")[0];
        } catch (IOException io) {
            io.printStackTrace();
        }
        this.s = s;
        this.isloggedin = true;
        System.out.println("Constructor end");
    }

//    private void setter(){
//        try {
//            System.out.println("try");
//            parts = dis.readUTF().split(":");
//            System.out.println("UTF passed");
//            clientName = parts[0];
//        } catch (IOException io) {
//            io.printStackTrace();
//            System.out.println("I am here");
//        }
//    }

    @Override
    public void run() {
        //setter(); //LANDMARK
        while (true) {
            try {
                String received = "";
                // receives the string
                try {
                    received = dis.readUTF();
                    System.out.println(received);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (received.contains("//exit//")) {
                    this.isloggedin = false;
                    this.s.close();
                    break;
                }

                // breaks the string into message and recipient part
                try {
                    StringTokenizer st = new StringTokenizer(received, "#");
                    String MsgToSend = st.nextToken();
                    String recipient = st.nextToken();

                    // searches for the recipient in the connected devices list.
                    // ar is the vector storing server of active users
                    for (ClientHandler mc : Server.clientList) {
                        // if avail recipient, to write on its stream
                        if (mc.name.equals(recipient) && mc.isloggedin) {
                            mc.dos.writeUTF(MsgToSend);
                            break;
                        }
                    }
                } catch (Exception e) {
                    for (ClientHandler mc : Server.clientList) {
                        //to write on all streams
                        mc.dos.writeUTF(received);
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try {
            // closing resources 
            this.dis.close();
            this.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 