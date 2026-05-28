package org.iesch.psp.TCP.EchoServerSHAyAES;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
//Su única misión es abrir el puerto de red, crear el almacén de estadísticas EchoData (solo una vez)
// y quedarse en un bucle continuo aceptando las llamadas de los clientes.
// Por cada llamada, delega el trabajo a un hilo secundario.
public class EchoServer {

    public static void main(String[] args) {
        int puerto = 5000;
        EchoData estadisticas = new EchoData();

        // Al usar try-with-resources, el socket se asegurará de cerrarse al final
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("EchoServer iniciado. Esperando clientes...");

            // Bucle que gira mientras el enchufe del servidor siga existiendo
            while (!serverSocket.isClosed()) {
                try {
                    Socket cliente = serverSocket.accept();

                    // INYECCIÓN: Le pasamos el cliente, los datos, y el propio ServerSocket
                    EchoServerThread trabajador = new EchoServerThread(cliente, estadisticas, serverSocket);
                    new Thread(trabajador).start();

                } catch (SocketException e) {
                    // TRUCO MAESTRO: Si un hilo cierra el serverSocket, el accept()
                    // lanza esta excepción. La capturamos para salir sin que salga en rojo.
                    System.out.println("El servidor ha sido apagado por una orden de parada.");
                    break; // Salimos del bucle while
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}