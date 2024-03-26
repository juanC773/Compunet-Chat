import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private ApplicationServer server;
    private String localUser;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            int option = Integer.parseInt(in.readLine());

            if (option == 1) {
                handleOption1();
            } else if (option == 2) {
                handleOption2();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Opcion 1. si el usuario desea crear una "cuenta" para iniciar sesión
    private void handleOption1() throws IOException {
        out.println("Digite el nombre de su nuevo usuario:");
        String newUsername = getUsername();

        while (ApplicationServer.searchUser(newUsername)) {
            out.println("Usuario existente, prueba otro!");
            newUsername = getUsername();
        }

        ApplicationServer.addUser(newUsername, clientSocket);
        localUser = newUsername;
        out.println("Agregado con éxito!\n");

        handleMenu();
    }


    //Opción 2, si el usuario ya tiene cuenta y desea iniciar sesión
    private void handleOption2() throws IOException {
        out.println("Digite su nombre de usuario:");
        String userTyped = in.readLine();

        while (!ApplicationServer.searchUser(userTyped)) {
            out.println("Usuario no existente...Prueba de nuevo");
            userTyped = in.readLine();
        }

        localUser = userTyped;
        handleMenu();
    }



    private String getUsername() throws IOException {
        return in.readLine();
    }


    //Menu de opciones
    private void handleMenu() throws IOException {
        while (true) {
            String menu = "---Welcome to WhatAsk----\n" +
                    "1. Crear grupo.\n" +
                    "2. Enviar mensaje a un usuario o grupo.\n" +
                    "3. Salir :( ";
            out.println(menu);

            int choice = Integer.parseInt(in.readLine());
            switch (choice) {
                case 1:
                    createGroup();
                    break;
                case 2:
                    sendMessage();
                    break;
                case 3:
                    out.println("Adiós!!");
                    return;
                default:
                    out.println("Opción inválida. Por favor, elige una opción válida.");
                    break;
            }
        }
    }






    private void createGroup() throws IOException {
        out.println("Digite el nombre del grupo:");
        String groupName = in.readLine();

        // Verificar si el grupo ya existe
        if (ApplicationServer.searchGroup(groupName)) {
            out.println("El grupo ya existe.");
            return;
        }

        // Crear un nuevo grupo
        ApplicationServer.addGroup(groupName, clientSocket);

        // Agregar al creador del grupo como miembro del grupo
        ApplicationServer.addUserToGroup(groupName, localUser);

        // Obtener la lista de usuarios existentes para que el usuario pueda elegir de ellos
        StringBuilder userSelectionPrompt = new StringBuilder("Seleccione usuarios para agregar al grupo (Ingrese el número de usuario seguido de coma ',' para seleccionar múltiples usuarios):\n");

        // Enumerar los usuarios existentes
        int userIndex = 1;

        for (String username : ApplicationServer.getUsers().keySet()) {
            // Excluir al usuario que crea el grupo de la lista
            if (!username.equals(localUser)) {
                userSelectionPrompt.append(userIndex).append(". ").append(username).append("\n");
                userIndex++;
            }
        }

        userSelectionPrompt.append("Ingrese 0 para finalizar la selección.");

        out.println(userSelectionPrompt.toString());

        // Leer la entrada del usuario para seleccionar los usuarios
        String input = in.readLine();
        String[] selectedUserIndexes = input.split(",");
        ArrayList<String> selectedUsers = new ArrayList<>();

        // Procesar las selecciones del usuario
        for (String indexStr : selectedUserIndexes) {
            int index = Integer.parseInt(indexStr.trim()) - 1; // El índice seleccionado menos 1 para ajustar a la lista
            if (index >= 0 && index < ApplicationServer.getUsers().size()) {
                String[] usernames = ApplicationServer.getUsers().keySet().toArray(new String[0]);
                selectedUsers.add(usernames[index]);
            }
        }

        // Agregar usuarios seleccionados al grupo
        for (String user : selectedUsers) {
            ApplicationServer.addUserToGroup(groupName, user);
        }

        out.println("\nEl grupo fue creado... mira sus integrantes:\n");
        List<String> groupMembers = ApplicationServer.getGroupMembers(groupName);
        for (String member : groupMembers) {
            out.println("- " + member);
        }
    }





    //Enviar un mensajes


    // envío de mensajes a usuarios
    private void sendMessageToUser() throws IOException {
        out.println("Enter recipient username:");
        String recipient = in.readLine();
        out.println("Enter your message:");
        String messageToSend = in.readLine();

        if (ApplicationServer.searchUser(recipient)) {
            Socket recipientSocket = ApplicationServer.getUsers().get(recipient);
            out.println(recipientSocket);
            out.println(recipient);
            PrintWriter recipientOut = new PrintWriter(recipientSocket.getOutputStream(), true);
            recipientOut.println("Message from " + localUser + ": " + messageToSend);
        } else {
            out.println("User does not exist. Please enter a valid username.");
        }
    }


    //envío de mensajes a grupos
    // Método para manejar el envío de mensajes a grupos
    private void sendMessageToGroup() throws IOException {
        out.println("Enter group name:");
        String groupName = in.readLine();
        out.println("Enter your message:");
        String messageToSend = in.readLine();

        if (ApplicationServer.searchGroup(groupName)) {
            List<String> groupMembers = ApplicationServer.getGroupMembers(groupName);

            // Verificar si el remitente es miembro del grupo
            if (groupMembers.contains(localUser)) {
                for (String member : groupMembers) {
                    Socket memberSocket = ApplicationServer.getUsers().get(member);
                    PrintWriter memberOut = new PrintWriter(memberSocket.getOutputStream(), true);
                    memberOut.println("Message from " + localUser + " in group " + groupName + ": " + messageToSend);
                }
            } else {
                out.println("You are not a member of this group.");
            }
        } else {
            out.println("Group does not exist. Please enter a valid group name.");
        }
    }




    // Método para manejar el envío de mensajes
    private void sendMessage() throws IOException {
        out.println("Choose message recipient type:");
        out.println("1. User");
        out.println("2. Group");

        int recipientType = Integer.parseInt(in.readLine());

        switch (recipientType) {
            case 1:
                sendMessageToUser();
                break;
            case 2:
                sendMessageToGroup();
                break;
            default:
                out.println("Invalid recipient type. Please choose a valid option.");
                break;
        }
    }













}
