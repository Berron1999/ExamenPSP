package EchoServerExamenAES;

import java.util.ArrayList;
import java.util.List;

/*
 * Launcher -> ejercicio 3 del enunciado.
 *
 * Lanza NUM_CLIENTES clientes en paralelo. Cada cliente envía
 * MENSAJES_POR_CLIENTE mensajes -> total: 100 * 100 = 10000.
 *
 * Se espera con join() a que todos los clientes terminen ANTES de imprimir
 * el tiempo total. Después se ejecuta manualmente EchoStopper para parar
 * el servidor y leer los contadores de EchoData (que deberían dar 10000
 * exactos gracias al synchronized).
 */
public class EchoClientLauncher {

    static final int NUM_CLIENTES = 100;
    static final int MENSAJES_POR_CLIENTE = 100;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("[Launcher AES] Lanzando " + NUM_CLIENTES + " clientes...");
        long tiempoInicio = System.currentTimeMillis();

        List<EchoClient> hilos = new ArrayList<>();

        for (int i = 1; i <= NUM_CLIENTES; i++) {
            // EchoClient extiende Thread -> se crea e inicia con start()
            EchoClient cliente = new EchoClient(i, MENSAJES_POR_CLIENTE);
            hilos.add(cliente);
            cliente.start();
        }

        // Esperar a que todos los hilos terminen
        for (EchoClient cliente : hilos) {
            cliente.join();
        }

        long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
        System.out.println("[Launcher AES] Todos los clientes han terminado en " + tiempoTotal + " ms.");
        System.out.println("[Launcher AES] Ahora ejecuta EchoStopper para parar el servidor.");
    }
}
