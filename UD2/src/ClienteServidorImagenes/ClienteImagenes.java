package ClienteServidorImagenes;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteImagenes {

    static final String HOST          = "localhost";
    static final int    PUERTO        = 5001;
    static final String CARPETA_LOCAL = "C:\\descargas_imagenes\\";

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        // Creamos la carpeta local de descargas si no existe
        new File(CARPETA_LOCAL).mkdirs();

        try (
                Socket socket        = new Socket(HOST, PUERTO);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream  in  = new DataInputStream(socket.getInputStream())
        ) {
            System.out.println("Conectado al servidor de imágenes.");

            boolean ejecutando = true;
            while (ejecutando) {
                System.out.println("\n1. Listar imágenes");
                System.out.println("2. Descargar imagen");
                System.out.println("3. Salir");
                System.out.print("Elige una opción: ");
                String opcion = teclado.nextLine().trim();

                switch (opcion) {
                    case "1":
                        out.writeUTF("LISTAR");
                        recibirListado(in);
                        break;

                    case "2":
                        System.out.print("Nombre de la imagen: ");
                        String nombre = teclado.nextLine().trim();
                        out.writeUTF("ENVIAR:" + nombre);
                        recibirImagen(in, nombre);
                        break;

                    case "3":
                        out.writeUTF("SALIR");
                        ejecutando = false;
                        System.out.println("Desconectando...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    // Recibe y muestra el listado de imágenes del servidor
    private static void recibirListado(DataInputStream in) throws IOException {
        String respuesta = in.readUTF(); // ej: "LISTA:3"
        int cantidad = Integer.parseInt(respuesta.split(":")[1]);

        if (cantidad == 0) {
            System.out.println("No hay imágenes disponibles.");
            return;
        }

        System.out.println("\n--- Imágenes disponibles (" + cantidad + ") ---");
        for (int i = 0; i < cantidad; i++) {
            System.out.println("  - " + in.readUTF());
        }
    }

    // Recibe los bytes de la imagen y la guarda en disco
    private static void recibirImagen(DataInputStream in, String nombre) throws IOException {
        // El servidor envía primero el tamaño en bytes
        long tamanio = in.readLong();

        if (tamanio == -1) {
            System.out.println("Error: imagen no encontrada en el servidor.");
            return;
        }

        // Leemos exactamente 'tamanio' bytes
        byte[] bytes = new byte[(int) tamanio];
        in.readFully(bytes); // readFully garantiza leer todos los bytes

        // Guardamos la imagen en la carpeta local
        File destino = new File(CARPETA_LOCAL + nombre);
        try (FileOutputStream fos = new FileOutputStream(destino)) {
            fos.write(bytes);
        }

        System.out.println("Imagen guardada en: " + destino.getAbsolutePath());
    }
}