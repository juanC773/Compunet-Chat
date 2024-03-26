import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatUser {
    private static final String address = "localHost";
    private static final int port = 9999;

    public static void main(String[] args) {
        try {

            Socket socket = new Socket(address, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);



            //Login
            System.out.println("1.Crear Usuario\n" +
                              "2.Iniciar sesión");
            String option = scanner.nextLine();
            out.println(option);




            // Leer mensajes del servidor en un hilo separado
            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();


            // Enviar mensajes al servidor
            String message;
            while (true) {
                message = scanner.nextLine();
                out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}