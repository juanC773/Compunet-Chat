import java.io.*;
import java.net.*;
import java.util.*;

public class ApplicationServer {

    private static final int SERVER_PORT = 9999;
    private static final Map<String, Socket> users = new HashMap<>();

    private static final Map<String, List<String>> groups = new HashMap<>();


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



    public static void addGroup(String groupName, Socket socket) {
        if (!groups.containsKey(groupName)) {
            groups.put(groupName, new ArrayList<>());
            System.out.println("Grupo creado con éxito: " + groupName);
        } else {
            System.out.println("El grupo '" + groupName + "' ya existe.");
        }
    }

    public static void addUserToGroup(String groupName, String username) {
        // Verificar si el grupo ya existe
        if (groups.containsKey(groupName)) {
            // Obtener la lista de miembros del grupo y agregar el usuario si no está presente
            List<String> members = groups.get(groupName);
            if (!members.contains(username)) {
                members.add(username);
                System.out.println("Usuario '" + username + "' agregado al grupo '" + groupName + "'.");
            } else {
                System.out.println("El usuario '" + username + "' ya está en el grupo '" + groupName + "'.");
            }
        } else {
            System.out.println("El grupo '" + groupName + "' no existe.");
        }
    }




    public static boolean searchGroup(String id) {
        return groups.containsKey(id);
    }



    public static List<String> getGroupMembers(String groupName) {
        return groups.getOrDefault(groupName, new ArrayList<>());
    }











}



