package EchoServer;

import java.util.ArrayList;
import java.util.List;

public class EchoCliente {

    static final String HOST                 = "localhost";
    static final int    PUERTO               = 5008;
    static final int    NUM_CLIENTES         = 100;
    static final int    MENSAJES_POR_CLIENTE = 100;

    // Contadores compartidos entre todos los hilos cliente
    static int mensajesEnviados  = 0;
    static int mensajesCorrectos = 0;

    // synchronized → solo un hilo incrementa a la vez, evita condición de carrera
    static synchronized void sumarEnviado()  { mensajesEnviados++; }
    static synchronized void sumarCorrecto() { mensajesCorrectos++; }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("[Main] Lanzando " + NUM_CLIENTES + " clientes en paralelo...");
        long tiempoInicio = System.currentTimeMillis();

        // Lanzamos NUM_CLIENTES hilos, cada uno simula un cliente independiente
        List<Thread> hilos = new ArrayList<>();
        for (int i = 1; i <= NUM_CLIENTES; i++) {
            Thread hilo = new Thread(new HiloCliente(i), "Cliente-" + i);
            hilos.add(hilo);
            hilo.start();
        }

        // El main espera a que TODOS los hilos cliente terminen antes de mostrar resultados
        for (Thread hilo : hilos) {
            hilo.join();
        }

        long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
        int  esperado    = NUM_CLIENTES * MENSAJES_POR_CLIENTE;

        System.out.println("\n=== RESULTADO FINAL ===");
        System.out.println("Mensajes enviados:    " + mensajesEnviados);
        System.out.println("Ecos correctos:       " + mensajesCorrectos);
        System.out.println("Total esperado:       " + esperado);
        System.out.println("Tiempo total:         " + tiempoTotal + " ms");
        System.out.println("Resultado: " + (mensajesCorrectos == esperado ? "CORRECTO ✔" : "ERROR ✘"));
    }
}