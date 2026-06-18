package TCPSHA256;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Cliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 6300;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Pedimos los datos al usuario
            System.out.print("Introduce tu nombre de usuario: ");
            String nombreUsuario = scanner.nextLine();

            System.out.print("Introduce tu mensaje: ");
            String mensaje = scanner.nextLine();

            // Conectamos al servidor
            Socket socket = new Socket(HOST, PUERTO);

            // Abrimos los streams
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Creamos el objeto con la fecha/hora actual y lo enviamos
            DatosMensaje datos = new DatosMensaje(nombreUsuario, mensaje, LocalDateTime.now());
            out.reset();
            out.writeObject(datos);

            // Leemos la respuesta del servidor
            RespuestaServidor respuesta = (RespuestaServidor) in.readObject();

            // Mostramos los datos recibidos
            System.out.println("\n--- Respuesta del servidor ---");
            System.out.println("Hash SHA-256: " + respuesta.getHashGenerado());
            System.out.println("Longitud del mensaje: " + respuesta.getNumeroCaracteresMensaje() + " caracteres");
            System.out.println("Fecha de recepción: " + respuesta.getFechaRecepcion());

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}