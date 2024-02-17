package app.bundle.casestudy.casetwo;

import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static app.bundle.casestudy.casetwo.constants.Constants.SOCKET_IP;
import static app.bundle.casestudy.casetwo.constants.Constants.SOCKET_PORT;

public class SocketClientApp {

    public static final int MD5_LAST_N_DIGIT = 2;
    public static final int DELAY_IN_MS_BETWEEN_DATA_GENERATIONS = 200; // 200ms delay means data is sent 5 times/second
    public static final int RANDOM_INT_UPPER_BOUND = 101;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SOCKET_IP, SOCKET_PORT);
            System.out.println("Connection established.");

            OutputStream outputStream = socket.getOutputStream();

            while (true) {
                String data = generateRandomData();
                outputStream.write(data.getBytes());
                System.out.println("Data is sent: " + data);
                Thread.sleep(DELAY_IN_MS_BETWEEN_DATA_GENERATIONS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateRandomData() throws NoSuchAlgorithmException {
        // Timestamp in milliseconds
        long timestamp = System.currentTimeMillis();

        Random random = new Random();
        int randomInt = random.nextInt(RANDOM_INT_UPPER_BOUND); // A random integer in range [0,RANDOM_INT_UPPER_BOUND-1].

        StringBuilder combinedData = new StringBuilder();
        combinedData.append(timestamp);
        combinedData.append(',');
        combinedData.append(randomInt);

        String hash = getMD5HashLastNDigits(combinedData.toString(), MD5_LAST_N_DIGIT);
        combinedData.append(',');
        combinedData.append(hash);
        combinedData.append('\n');

        return combinedData.toString();
    }

    public static String getMD5HashLastNDigits(String data, int n) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data.getBytes());
        byte[] digest = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff)); // converts each byte to 2 hexadecimal digits in lowercase
        }

        return sb.substring(sb.length() - n); // returns last 2 chars of MD5 hash
    }
}
