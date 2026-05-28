package EchoServerExamenRSA;

import java.util.ArrayList;
import java.util.List;

/*
 * Launcher RSA: lanza 100 clientes en paralelo, cada uno cifra con RSA.
 *
 * NOTA: RSA es MUY LENTO comparado con AES o con texto plano. Cada cliente
 * tarda en generar su par de claves de 2048 bits, y cada cifrado/descifrado
 * RSA es órdenes de magnitud más caro. La ejecución completa con 10000
 * mensajes puede tardar bastante más que las versiones con AES/SHA.
 */
public class EchoClientLauncher {

    static final int NUM_CLIENTES = 100;
    static final int MENSAJES_POR_CLIENTE = 100;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("[Launcher RSA] Lanzando " + NUM_CLIENTES + " clientes...");
        long tiempoInicio = System.currentTimeMillis();

        List<EchoClient> hilos = new ArrayList<>();

        for (int i = 1; i <= NUM_CLIENTES; i++) {
            EchoClient cliente = new EchoClient(i, MENSAJES_POR_CLIENTE);
            hilos.add(cliente);
            cliente.start();
        }

        for (EchoClient cliente : hilos) {
            cliente.join();
        }

        long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
        System.out.println("[Launcher RSA] Todos los clientes han terminado en " + tiempoTotal + " ms.");
        System.out.println("[Launcher RSA] Ahora ejecuta EchoStopper para parar el servidor.");
    }
}
