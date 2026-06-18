package VotacionElectronicaSincronizada;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

public class ServidorVotacion {

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(7800);
            System.out.println("Servidor de votacion escuchando en el puerto 7800...");

            while (true) {
                System.out.println("Esperando una nueva ronda de 3 votantes...");

                CyclicBarrier barrera = new CyclicBarrier(3); // nueva barrera por cada ronda
                ContadorVotos contador = new ContadorVotos(); // nuevo contador por cada ronda

                for (int i = 0; i < 3; i++) {
                    Socket cliente = servidor.accept();
                    System.out.println("Votante conectado: " + cliente.getInetAddress());

                    ManejadorVoto manejador = new ManejadorVoto(cliente, barrera, contador);
                    manejador.start();
                }
                // al terminar el for, esta ronda ya tiene sus 3 hilos lanzados
                // el bucle while vuelve a empezar para esperar a los 3 votantes de la siguiente ronda
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}