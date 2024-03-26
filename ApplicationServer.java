import java.io.*;
import java.net.*;
import java.util.*;

public class ApplicationServer {

    private static final int SERVER_PORT = 9999;
    private static final Map<String, Socket> users = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started...");






            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);

                System.out.println("New client connected: " + socket);

                // Inicia un nuevo hilo para manejar al cliente
                Thread clientHandler = new Thread(new ClientHandler(socket));
                clientHandler.start();



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Map<String, Socket> getUsers() {
        return users;
    }


    public static void addUser(String username, Socket socket) {


        users.put(username, socket);
        System.out.println("Usuario añadido con exito!");

    }


    public static  boolean searchUser(String username){
        return users.containsKey(username);
    }





}



