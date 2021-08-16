import client.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import properties.Props;
import server.Server;

import java.io.*;
import java.net.ServerSocket;

public class Tests {

    @Test
    public void serverStartedTest() {
        try {
            ServerSocket server = new ServerSocket(Props.getPort());
            Assertions.assertTrue(!server.isClosed(), "the server was not started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void serverLogTest() {
        String testMsg = "Test message";
        Server.log(testMsg);
        File log = new File("src/main/resources/server.log");
        BufferedReader input;
        String actual = null;
        String line;
        try {
            input = new BufferedReader(new FileReader(log));
            while ((line = input.readLine()) != null) {
                actual = line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(actual.contains(testMsg),"no logs were recorded");
    }

    @Test
    public void clientLogTest(){
        String testMsg = "Test message";
        Client.log(testMsg);
        File log = new File("src/main/resources/client.log");
        BufferedReader input;
        String actual = null;
        String line;
        try {
            input = new BufferedReader(new FileReader(log));
            while ((line = input.readLine()) != null) {
                actual = line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(actual.contains(testMsg), "no logs were recorded");
    }

    @Test
    public void hostFromFileTest(){
        String expected = "localhost";
        String actual = Props.getHost();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void portFromFileTest(){
        int expected = 24333;
        int actual = Props.getPort();
        Assertions.assertEquals(expected, actual);
    }
}
