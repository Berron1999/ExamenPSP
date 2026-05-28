package ServidorDeTurnos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorTurnos {

    static final int PUERTO = 5003;

    // Semáforo con 1 permiso → solo un cliente es atendido a la vez
    static final Semaphore semaforo = new Semaphore(1);

    // Contador atómico para asignar números de turno (seguro entre hilos)
    static final AtomicInteger contadorTurnos = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("Servidor de turnos iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                // Asignamos turno en el momento de la conexión
                int turno = contadorTurnos.incrementAndGet();
                System.out.println("Cliente conectado. Turno asignado: " + turno);
                new Thread(new HiloClienteTurnos(socketCliente, turno)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}