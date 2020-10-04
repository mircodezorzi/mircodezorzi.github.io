import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    try (
      Socket socket = new Socket("127.0.0.1", 8080);
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    ) {
      String input = null;
      String received = null;
      while (scanner.hasNext()){
        input = scanner.nextLine();
        out.println(input);
        received = in.readLine();
        System.out.println("Server reply: " + received);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
