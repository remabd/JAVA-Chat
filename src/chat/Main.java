package chat;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import chat.ClientThread;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class Main {
  public static ArrayList<ClientThread> clients = new ArrayList<>();

  public static void main(String[] args) {
    int port = 5156;

    try {
      ServerSocket server = new ServerSocket(port);

      while (true) {
        try {
          Socket client = server.accept();
          ClientThread clientThread = new ClientThread(client);
          Main.clients.add(clientThread);
          clientThread.start();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    } catch (IOException e) {
      System.out.println(e);
      e.printStackTrace();
    }
  }
}
