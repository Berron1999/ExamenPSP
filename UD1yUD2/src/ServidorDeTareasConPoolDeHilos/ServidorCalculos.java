package ServidorDeTareasConPoolDeHilos;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.math.BigInteger;
import java.util.concurrent.*;

public class ServidorCalculos {

    static final int PUERTO        = 5005;
    static final int HILOS_EN_POOL = 3; // máximo 3 cálculos simultáneos

    // Pool fijo de 3 hilos: si llegan más tareas, esperan en cola
    static final ExecutorService pool = Executors.newFixedThreadPool(HILOS_EN_POOL);

    public static void main(String[] args) {
        System.out.println("Servidor de cálculos iniciado. Pool de " + HILOS_EN_POOL + " hilos.");

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                // Cada cliente se atiende en un hilo independiente del pool
                new Thread(() -> atenderCliente(socketCliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void atenderCliente(Socket socket) {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String linea;
            while ((linea = entrada.readLine()) != null) {

                if (linea.equals("SALIR")) break;

                int numero = Integer.parseInt(linea.trim());
                System.out.println("[Servidor] Petición recibida: factorial(" + numero + ")");

                // submit() envía la tarea al pool y devuelve un Future inmediatamente
                // El hilo del servidor no se bloquea esperando: el pool lo gestiona
                Future<BigInteger> future = pool.submit(new TareaFactorial(numero));

                // future.get() SÍ bloquea hasta que el cálculo termine
                // pero solo bloquea este hilo de cliente, no el servidor principal
                BigInteger resultado = future.get();
                salida.println("factorial(" + numero + ") = " + resultado);
            }

        } catch (IOException | InterruptedException | ExecutionException e) {
            System.out.println("Error con cliente: " + e.getMessage());
        }
    }
}