package ChatMultiusuario;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.PrintWriter;

public class ServidorChat {

    static final int PUERTO = 5004;

    // Lista sincronizada de los PrintWriter de todos los clientes conectados
    // Collections.synchronizedList → evita problemas al añadir/eliminar desde varios hilos
    static final List<PrintWriter> clientes = Collections.synchronizedList(new ArrayList<>());

    // Envía un mensaje a TODOS los clientes conectados (broadcast)
    static void broadcast(String mensaje) {
        synchronized (clientes) {
            for (PrintWriter pw : clientes) {
                pw.println(mensaje);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Servidor de chat iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                new Thread(new HiloChat(socketCliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}