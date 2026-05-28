package org.iesch.psp.TCP.VentaDeEntradas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteComprador implements Runnable {
    private String idTerminal;

    public ClienteComprador(String idTerminal) {
        this.idTerminal = idTerminal;
    }

    // SIN la etiqueta @Override
    public void run() {
        String host = "localhost";
        int puerto = 9000;

        try (Socket socket = new Socket(host, puerto);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Creamos la petición de 15 entradas
            PeticionCompra peticion = new PeticionCompra(idTerminal, 15);

            // La enviamos por el túnel TCP
            out.writeObject(peticion);

            // Leemos la respuesta del servidor y la imprimimos
            String respuesta = (String) in.readObject();
            System.out.println(idTerminal + " -> " + respuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}