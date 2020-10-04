import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ClientHandler extends Thread {
  String name = "anon";
  String ip = null;

  Socket socket = null;
  PrintWriter out = null;
  BufferedReader in = null;

  public ClientHandler(Socket socket) {
    this.socket = socket;
    this.ip = ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().toString().replace("/","");

    try {
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      broadcast(ip + " has just jointed!", false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      out.println("Your are now connected to our server!");
      String received = null;
      while ((received = in.readLine()) != null) {
        switch (received.split(" ")[0]) {
          case "/name":
            if (received.split(" ").length > 1) {
              broadcast(name + " changed name to " + received.split(" ")[1], false);
              name = received.split(" ")[1];
            }
            out.println("changed name to " + name);
            break;
          case "/quit":
            socket.close();
            broadcast(ip + " has just left!", false);
            break;
          default:
            broadcast(name + "\t" + received, false);
        }
      }
    } catch (java.net.SocketException e) {
      interrupt();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void interrupt() {
    try {
      in.close();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void send(String message) {
    out.println(message);
  }

  public void broadcast(String message, boolean self) {
    for (ClientHandler connection : ServerUsers.connections) {
      if (connection != this || self) {
        connection.send(message);
      }
    }
  }
}

public class ServerUsers {

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
