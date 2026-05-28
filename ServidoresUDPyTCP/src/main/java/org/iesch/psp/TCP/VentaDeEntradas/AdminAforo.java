package org.iesch.psp.TCP.VentaDeEntradas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AdminAforo {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 9000;

        System.out.println("Iniciando terminal de administrador...");

        try (Socket socket = new Socket(host, puerto);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Conectado al teatro. Solicitando aforo...");

            // TRUCO: Enviamos un objeto PeticionCompra, pero con el ID "INFO" y 0 entradas
            PeticionCompra peticionAdmin = new PeticionCompra("INFO", 0);
            out.writeObject(peticionAdmin);

            String respuesta = (String) in.readObject();
            System.out.println("Respuesta del servidor: " + respuesta);

        } catch (Exception e) {
            System.err.println("Error: No se pudo conectar con el Servidor de Ventas.");
            e.printStackTrace();
        }
    }
}