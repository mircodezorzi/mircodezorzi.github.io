import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(String[] args) {
    try (
      ServerSocket serverSocket = new ServerSocket(8080);
      Socket socket = serverSocket.accept();
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    ) {
      String received = null;
      while ((received = in.readLine()) != null) {
        System.out.println("Received message: " + received);
        if (received.equals("exit")) {
          break;
        }
        out.println("Roger!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
