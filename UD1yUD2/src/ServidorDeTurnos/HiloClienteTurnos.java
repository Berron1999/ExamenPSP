package ServidorDeTurnos;

import ServidorDeTurnos.ServidorTurnos;

import java.io.*;
import java.net.Socket;

public class HiloClienteTurnos implements Runnable {

    private Socket socket;
    private int turno;

    public HiloClienteTurnos(Socket socket, int turno) {
        this.socket = socket;
        this.turno  = turno;
    }

    @Override
    public void run() {
        try (
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String nombre = entrada.readLine(); // recibimos el nombre del cliente

            // Informamos al cliente de su turno y cuántos hay esperando antes que él
            int esperando = ServidorTurnos.semaforo.getQueueLength();
            salida.println("Hola " + nombre + ". Tu turno es el " + turno
                    + ". Clientes esperando antes que tú: " + esperando);

            // acquire() bloquea este hilo hasta que el semáforo tenga un permiso libre
            // Si hay otro cliente siendo atendido, este hilo espera aquí
            ServidorTurnos.semaforo.acquire();

            // --- SECCIÓN CRÍTICA: solo un cliente entra aquí a la vez ---
            salida.println("Turno " + turno + ": ¡Es tu turno! Siendo atendido...");
            System.out.println("Atendiendo turno " + turno + " (" + nombre + ")");

            // Simulamos el tiempo de atención (3 segundos)
            Thread.sleep(3000);

            salida.println("Turno " + turno + ": Atención finalizada. ¡Hasta pronto, " + nombre + "!");
            System.out.println("Turno " + turno + " finalizado.");
            // --- FIN SECCIÓN CRÍTICA ---

        } catch (IOException | InterruptedException e) {
            System.out.println("Error con cliente turno " + turno + ": " + e.getMessage());
        } finally {
            // release() siempre se ejecuta, libera el semáforo para el siguiente cliente
            ServidorTurnos.semaforo.release();
            try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}