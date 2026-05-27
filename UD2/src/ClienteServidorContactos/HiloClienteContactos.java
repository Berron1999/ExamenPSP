package ClienteServidorContactos;

import ClienteServidorContactos.Contacto;
import ClienteServidorContactos.ServidorContactos;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HiloClienteContactos implements Runnable {

    private Socket socket;

    public HiloClienteContactos(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                // ObjectOutputStream/ObjectInputStream para enviar objetos serializados
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream  in  = new ObjectInputStream(socket.getInputStream())
        ) {
            String peticion;
            while (!(peticion = (String) in.readObject()).equals("SALIR")) {

                if (peticion.equals("LISTAR")) {
                    // Enviamos la lista completa de contactos como objeto
                    out.reset();
                    out.writeObject(new ArrayList<>(ServidorContactos.contactos));

                } else if (peticion.startsWith("BUSCAR:")) {
                    String nombre = peticion.substring("BUSCAR:".length()).trim().toLowerCase();
                    Contacto encontrado = ServidorContactos.contactos.stream()
                            .filter(c -> c.getNombre().toLowerCase().contains(nombre))
                            .findFirst()
                            .orElse(null); // null si no existe

                    // Enviamos el contacto encontrado (o null si no existe)
                    out.reset();
                    out.writeObject(encontrado);
                }
            }
            System.out.println("Cliente desconectado: " + socket.getInetAddress());

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error con cliente: " + e.getMessage());
        }
    }
}