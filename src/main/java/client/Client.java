package client;

import properties.Props;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {

    public static void main(String[] args) {
        new ClientConnection(Props.getHost(), Props.getPort());
    }

    public static void log(String msg) {
        String dateNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        File log = new File("src/main/resources/client.log");
        try (FileWriter fw = new FileWriter(log, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(String.format("[%s] %s\n", dateNow, msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
