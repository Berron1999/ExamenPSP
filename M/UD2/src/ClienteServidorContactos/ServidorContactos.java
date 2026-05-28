package ClienteServidorContactos;

import ClienteServidorContactos.Contacto;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorContactos {

    static final int PUERTO = 5002;

    // Lista de contactos compartida (la "base de datos" del servidor)
    static final List<Contacto> contactos = new ArrayList<>();

    static {
        // Cargamos los contactos al arrancar el servidor
        contactos.add(new Contacto("Ana García",    "600111222", "ana@email.com"));
        contactos.add(new Contacto("Luis Martínez", "611333444", "luis@email.com"));
        contactos.add(new Contacto("María López",   "622555666", "maria@email.com"));
        contactos.add(new Contacto("Carlos Ruiz",   "633777888", "carlos@email.com"));
    }

    public static void main(String[] args) {
        System.out.println("Servidor de contactos iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Cliente conectado: " + socketCliente.getInetAddress());
                // Un hilo por cliente para atender múltiples conexiones simultáneas
                new Thread(new HiloClienteContactos(socketCliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}