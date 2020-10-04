import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ClientHandler extends Thread {
  Socket socket = null;
  PrintWriter out = null;
  BufferedReader in = null;

  public ClientHandler(Socket socket) {
    this.socket = socket;

    try {
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    try {
      out.println("Your are now connected to our server!");
      String received = null;
      while ((received = in.readLine()) != null) {
        System.out.println(received);
        broadcast(received);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void send(String message) {
    out.println(message);
  }

  public void broadcast(String message) {
    for (ClientHandler connection : ServerBroadcast.connections) {
      connection.send(message);
    }
  }
}

public class ServerBroadcast {

  public static ArrayList<ClientHandler> connections = new ArrayList<ClientHandler>();

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

      ClientHandler t = new ClientHandler(socket);
      t.start();
      connections.add(t);
    }
  }
}
