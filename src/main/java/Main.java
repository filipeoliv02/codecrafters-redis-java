import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        System.out.println("Logs from your program will appear here!");

        ServerSocket serverSocket = null;
        Socket clientSocket;
        int port = 6379;
        String pong = "+PONG\r\n";

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while (true) {
                // Wait for connection from client.
                clientSocket = serverSocket.accept();

                // Handle multiple PING commands from the same connection.
                handlePingCommands(clientSocket, pong);

                // Close the client socket after handling the PING commands.
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }

    private static void handlePingCommands(Socket clientSocket, String pong) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream outputStream = clientSocket.getOutputStream();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("PING")) {
                outputStream.write(pong.getBytes());
                outputStream.flush();
            }
        }
    }
}