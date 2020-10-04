import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientListener extends Thread {
  Socket socket = null;
  BufferedReader in = null;

  public ClientListener(Socket socket) {
    this.socket = socket;

    try {
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    String received = null;
    try {
      while ((received = in.readLine()) != null) {
        System.out.println(received.replace(".*?\t.*?", "$1 | $2"));
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
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

public class ClientThreaded {

  public static void main(String args[]) {
    Scanner scanner = new Scanner(System.in);

    try {
      Socket socket = new Socket("127.0.0.1", 8080);
      ClientListener listener = new ClientListener(socket);
      listener.start();

      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      while (scanner.hasNext()) {
        String received = scanner.nextLine();
        out.println(received);
        if (received.equals("/quit")) {
          out.close();
          socket.close();
          break;
        }
      }

      listener.interrupt();
      listener.wait();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
