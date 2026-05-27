package ClienteServidorImagenes;

import ClienteServidorImagenes.ServidorImagenes;

import java.io.*;
import java.net.Socket;

public class HiloClienteImagenes implements Runnable {

    private Socket socket;

    public HiloClienteImagenes(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                // DataOutputStream/DataInputStream permiten mezclar texto y bytes binarios
                DataOutputStream salida  = new DataOutputStream(socket.getOutputStream());
                DataInputStream  entrada = new DataInputStream(socket.getInputStream())
        ) {
            String peticion;
            // readUTF() lee una cadena de texto enviada con writeUTF()
            while (!(peticion = entrada.readUTF()).equals("SALIR")) {

                if (peticion.equals("LISTAR")) {
                    procesarListar(salida);

                } else if (peticion.startsWith("ENVIAR:")) {
                    String nombreImagen = peticion.substring("ENVIAR:".length());
                    procesarEnviarImagen(salida, nombreImagen);
                }
            }
            System.out.println("Cliente desconectado: " + socket.getInetAddress());

        } catch (IOException e) {
            System.out.println("Error con cliente: " + e.getMessage());
        }
    }

    // Envía la lista de imágenes .jpg y .png disponibles
    private void procesarListar(DataOutputStream salida) throws IOException {
        File directorio = new File(ServidorImagenes.DIRECTORIO);
        File[] imagenes = directorio.listFiles(f ->
                f.isFile() && (f.getName().endsWith(".jpg") || f.getName().endsWith(".png"))
        );

        if (imagenes == null || imagenes.length == 0) {
            salida.writeUTF("LISTA:0"); // indica que hay 0 imágenes
            return;
        }

        // Primero enviamos cuántas imágenes hay, luego sus nombres
        salida.writeUTF("LISTA:" + imagenes.length);
        for (File img : imagenes) {
            salida.writeUTF(img.getName());
        }
        salida.flush();
    }

    // Lee el archivo de imagen y lo envía como bytes al cliente
    private void procesarEnviarImagen(DataOutputStream salida, String nombreImagen) throws IOException {
        File imagen = new File(ServidorImagenes.DIRECTORIO + nombreImagen);

        if (!imagen.exists()) {
            salida.writeLong(-1); // -1 indica error: fichero no encontrado
            return;
        }

        // Leemos todos los bytes del fichero de imagen
        byte[] bytes = new FileInputStream(imagen).readAllBytes();

        // Primero enviamos el tamaño para que el cliente sepa cuántos bytes leer
        salida.writeLong(bytes.length);
        // Luego enviamos los bytes de la imagen
        salida.write(bytes);
        salida.flush();

        System.out.println("Imagen enviada: " + nombreImagen + " (" + bytes.length + " bytes)");
    }
}