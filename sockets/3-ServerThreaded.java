import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandler extends Thread {
  Socket socket = null;

  public ClientHandler(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    System.out.println("connection acquired!");

    try {
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      out.println("Your are now connected to our server!");

      String received = in.readLine();
      System.out.println(received);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

public class ServerThreaded {
  public static void main(String args[]) {
    ServerSocket serverSocket = null;
    Socket socket = null;

    try {
      serverSocket = new ServerSocket(8080);
    } catch (IOException e) {
      e.printStackTrace();
    }

    while (true) {
      try {
        socket = serverSocket.accept();
      } catch (IOException e) {
        e.printStackTrace();
      }
      new ClientHandler(socket).start();
    }
  }
}
