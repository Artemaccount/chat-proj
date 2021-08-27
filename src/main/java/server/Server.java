package server;

import properties.Props;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private static List<ServerConnection> serverList = new CopyOnWriteArrayList<>();

    public static List<ServerConnection> getServerList() {
        return serverList;
    }

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(Props.getPort());){
            log("Server started");
            System.out.println("Server started");
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client accepted");
                log("Client accepted");
                try {
                    serverList.add(new ServerConnection(socket));
                } catch (IOException e) {
                    log("Connection closed");
                    socket.close();
                }
            }
        } finally {
            log("Connection closed");
        }
    }

    public static void log(String msg) {
        String dateNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        File log = new File("src/main/resources/server.log");
        try (FileWriter fw = new FileWriter(log, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(String.format("[%s] %s\n", dateNow, msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}