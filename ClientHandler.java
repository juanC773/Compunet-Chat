import java.io.*;
import java.net.Socket;



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




            int option= Integer.parseInt(in.readLine());


            //---------------------------------------------------

            if (option==1){

                String message="Digite el nombre de su nuevo usuario:";

                out.println(message);

                String newUsername=in.readLine();


                while (ApplicationServer.searchUser(newUsername)){

                    out.println("Usuario existente, prueba otro!");
                    newUsername=in.readLine();

                }

                ApplicationServer.addUser(newUsername,clientSocket);

                localUser=newUsername;

                out.println("Agregado con éxito!\n");



                while (true) {
                    // Muestra el menú
                    String menu = "---Welcome to WhatAsk----\n" +
                            "1. Crear grupo.\n" +
                            "2. Enviar mensaje a un usuario o grupo.\n" +
                            "3. Salir :( ";

                    out.println(menu);

                    // Espera la opción seleccionada por el cliente
                    int choice = Integer.parseInt(in.readLine());


                    switch (choice) {

                        case 1:
                            System.out.println("Digite el nombre de los usuarios que quieres agregar separados por coma (santiago,juan,luna)");
                            break;
                        case 2:


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

                            break;



                        case 3:
                            // Salir del bucle y cerrar la conexión
                            return;
                        default:
                            out.println("Opción inválida. Por favor, elige una opción válida.");
                            break;
                    }


                }








                ///----------------------------------------------------

            }else if (option==2){


                String login="Digite su nombre de usuario:";
                out.println(login);
                String userTyped=in.readLine();


                while (ApplicationServer.searchUser(userTyped)==false){


                    out.println("Usuario no existente...Prueba de nuevo");
                    String userConfirmation=in.readLine();

                }




                while (true) {
                    // Muestra el menú
                    String menu = "---Welcome to WhatAsk----\n" +
                            "1. Crear grupo.\n" +
                            "2. Enviar mensaje a un usuario o grupo.\n" +
                            "3. Salir :( ";

                    out.println(menu);

                    // Espera la opción seleccionada por el cliente
                    int choice = Integer.parseInt(in.readLine());


                    switch (choice) {

                        case 1:
                            // Lógica para crear grupo
                            break;
                        case 2:


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

                            break;



                        case 3:
                            // Salir del bucle y cerrar la conexión
                            return;
                        default:
                            out.println("Opción inválida. Por favor, elige una opción válida.");
                            break;
                    }


                }








            }










        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
