package app.bundle.casestudy.casetwo;

import app.bundle.casestudy.casetwo.connectors.ActiveMQProducer;
import app.bundle.casestudy.casetwo.models.RandomData;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static app.bundle.casestudy.casetwo.constants.Constants.SOCKET_PORT;

public class SocketServerApp {

    public static final int MIN_RANDOM_VALUE_FOR_MQ = 90;
    public static final String OUTPUT_FILE_NAME = "output.txt";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SOCKET_PORT);
            System.out.println("Server is up and listening port " + SOCKET_PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client is connected");

                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private final ActiveMQProducer producer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            producer = new ActiveMQProducer();
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Data received: " + line);
                    RandomData data = new RandomData(line);

                    if (data.getRandom() > MIN_RANDOM_VALUE_FOR_MQ) { // If random>MIN_RANDOM_VALUE_FOR_MQ, then send the line to MQ
                        producer.getMessageProducer().send(producer.getSession().createTextMessage(line));
                        System.out.println("Data is sent to MQ: " + line);
                    } else { // otherwise, append to file
                        appendToFile(line);
                        System.out.println("Data is appended to the file.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void appendToFile(String line) {
            BufferedWriter writer = null;
            FileWriter fw = null;
            try {
                fw = new FileWriter(OUTPUT_FILE_NAME, true);
                writer = new BufferedWriter(fw);
                writer.write(line);
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ignored) {}
                }
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException ignored) {}
                }
            }
        }
    }
}
