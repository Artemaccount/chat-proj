package server;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerConnection extends Thread implements Closeable {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;


    public ServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        serverList.add(this);
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
        for (ServerConnection sc : Server.getServerList()) {
            try {
                sc.out.write(msg + "\n");
                sc.out.flush();
            } catch (IOException ignored) {
                Server.log(msg);
            }
        }
    }

    @Override
    public void close() {
        Server.log("server closed");
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerConnection sc : Server.getServerList()) {
                    if (sc.equals(this)) sc.interrupt();
                    Server.getServerList().remove(this);
                }
            }
        } catch (IOException e) {
            Server.log(e.getMessage());
        }
    }
}
