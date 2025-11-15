package chat;

import java.net.Socket;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientThread extends Thread {
  private Socket client;
  private String name;
  private BufferedReader bs;
  private DataOutputStream outchan;
  // private ClientController clientController;
  public static final String CLEAR_LINE = "\033[2K"; // Clear entire line
  public static final String CURSOR_UP = "\033[1A"; // Move cursor up 1 line
  public static final String CURSOR_START = "\r"; // Return to start of line

  public ClientThread(Socket s) {
    this.client = s;
    this.name = "";
    // this.clientController = cc;
  }

  private void initialize() {
    try {
      System.out.println("New user connected, waiting for name attribution");
      String line;
      boolean finis = false;
      this.outchan.writeChars("Bienvenue, entrez votre pseudo:\n");
      do {
        line = this.bs.readLine();
        if (line.length() > 0) {
          finis = true;
          this.name = line;
          this.outchan.writeChars("Bonjour " + line + "\n");
          // this.outchan.writeChars("confirmez vous votre pseudo: " + line + "(Y/n)");
        }
      } while (!finis);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    try {
      InputStream is = this.client.getInputStream();
      this.bs = new BufferedReader(new InputStreamReader(is));
      this.outchan = new DataOutputStream(this.client.getOutputStream());
      this.initialize();
      String line;
      do {
        line = this.bs.readLine();
        this.sendMessage(line);
        System.out.print(CURSOR_UP + CURSOR_START + CLEAR_LINE);
        System.out.println(line);
      } while (line != "exit");
      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendMessage(String line) {
    String message = this.name + ": " + line + "\n";
    Main.clients.stream().forEach(client -> client.display(message));
    System.out.println(message);
  }

  public void display(String message) {
    try {
      this.outchan.writeChars(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
