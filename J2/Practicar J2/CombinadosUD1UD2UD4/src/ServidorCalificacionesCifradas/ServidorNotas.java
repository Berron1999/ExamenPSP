package ServidorCalificacionesCifradas;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorNotas {

    public static void main(String[] args) {
        try {
            ExecutorService pool = Executors.newFixedThreadPool(4); // hasta 4 profesores en paralelo

            ServerSocket servidor = new ServerSocket(7200);
            System.out.println("Servidor de notas escuchando en el puerto 7200...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Profesor conectado: " + cliente.getInetAddress());

                pool.execute(new TareaProfesor(cliente));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}