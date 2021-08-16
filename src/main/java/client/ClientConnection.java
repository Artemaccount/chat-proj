package client;

import java.net.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientConnection {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String ip;
    private int port;
    private String nickname;

    public ClientConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            this.socket = new Socket(ip, port);
            Client.log("Connection started");
        } catch (IOException e) {
            Client.log(e.getMessage());
            e.printStackTrace();
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.enterNickname();
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            ClientConnection.this.close();
        }
    }

    private void enterNickname() {
        System.out.print("Enter your nickname: ");
        try {
            String dateNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            nickname = inputUser.readLine();
            String joined = ("(" + dateNow + ") " + nickname + " joined the chat" + "\n");
            out.write(joined);
            Client.log(joined);
            out.flush();
        } catch (IOException ignored) {
        }
    }

    private void close() {
        Client.log("Connection closed");
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            Client.log(e.getMessage());
        }
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals("/exit")) {
                        Client.log(str);
                        ClientConnection.this.close();
                        break;
                    }
                    Client.log(str);
                    System.out.println(str); // пишем сообщение с сервера на консоль
                }
            } catch (IOException e) {
                Client.log(e.getMessage());
                ClientConnection.this.close();
            }
        }
    }

    public class WriteMsg extends Thread {

        @Override
        public void run() {
            while (true) {
                String userInput;
                try {
                    String dateNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    userInput = inputUser.readLine();
                    if (userInput.equals("/exit")) {
                        Client.log(userInput);
                        out.write("/exit" + "\n");
                        ClientConnection.this.close();
                        break;
                    } else {
                        String inputWithNickname = ("(" + dateNow + ") " + nickname + ": " + userInput + "\n");
                        out.write(inputWithNickname);
                        Client.log(inputWithNickname);
                    }
                    out.flush();
                } catch (IOException e) {
                    Client.log(e.getMessage());
                    ClientConnection.this.close();

                }
            }
        }
    }
}
