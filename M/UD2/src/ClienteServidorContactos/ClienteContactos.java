package ClienteServidorContactos;

import ClienteServidorContactos.Contacto;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClienteContactos {

    static final String HOST   = "localhost";
    static final int    PUERTO = 5002;

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        try (
                Socket socket          = new Socket(HOST, PUERTO);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream  in  = new ObjectInputStream(socket.getInputStream())
        ) {
            System.out.println("Conectado al servidor de contactos.");

            boolean ejecutando = true;
            while (ejecutando) {
                System.out.println("\n1. Listar contactos");
                System.out.println("2. Buscar contacto");
                System.out.println("3. Salir");
                System.out.print("Elige una opción: ");
                String opcion = teclado.nextLine().trim();

                switch (opcion) {
                    case "1":
                        // Enviamos la petición como String serializado
                        out.reset();
                        out.writeObject("LISTAR");

                        // Recibimos la lista de contactos
                        List<Contacto> lista = (List<Contacto>) in.readObject();
                        System.out.println("\n--- Contactos (" + lista.size() + ") ---");
                        lista.forEach(c -> System.out.println("  " + c));
                        break;

                    case "2":
                        System.out.print("Nombre a buscar: ");
                        String nombre = teclado.nextLine().trim();

                        out.reset();
                        out.writeObject("BUSCAR:" + nombre);

                        // Recibimos un Contacto o null
                        Contacto contacto = (Contacto) in.readObject();
                        if (contacto != null) {
                            System.out.println("\n--- Contacto encontrado ---");
                            System.out.println("  " + contacto);
                        } else {
                            System.out.println("Contacto no encontrado.");
                        }
                        break;

                    case "3":
                        out.reset();
                        out.writeObject("SALIR");
                        ejecutando = false;
                        System.out.println("Desconectando...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}