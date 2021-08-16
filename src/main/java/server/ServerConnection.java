package server;

import java.io.*;
import java.net.*;

public class ServerConnection extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;


    public ServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String input;
        try {
                while (true) {
                    input = in.readLine();
                    if (input.equals("/exit")) {
                        Server.log(input);
                        this.close();
                        break;
                    }
                    sendToAll(input);
                }
        } catch (IOException e) {
            Server.log(e.getMessage());
            this.close();
        }
    }

    private void sendToAll(String msg) {
        Server.log(msg);
        for (ServerConnection sc : Server.serverList) {
            try {
                sc.out.write(msg + "\n");
                sc.out.flush();
            } catch (IOException ignored) {
            }
        }
    }

    private void close() {
        Server.log("server closed");
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerConnection sc : Server.serverList) {
                    if (sc.equals(this)) sc.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException e) {
            Server.log(e.getMessage());
        }
    }
}