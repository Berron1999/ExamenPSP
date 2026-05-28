package EchoServerExamenSecureRandom;

import java.util.ArrayList;
import java.util.List;

/*
 * Launcher: lanza 100 clientes en paralelo, cada uno con 100 mensajes
 * cifrados con AES/CBC e IV aleatorio (SecureRandom). Total: 10000.
 */
public class EchoClientLauncher {

    static final int NUM_CLIENTES = 100;
    static final int MENSAJES_POR_CLIENTE = 100;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("[Launcher SecureRandom] Lanzando " + NUM_CLIENTES + " clientes...");
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
        System.out.println("[Launcher SecureRandom] Todos los clientes han terminado en " + tiempoTotal + " ms.");
        System.out.println("[Launcher SecureRandom] Ahora ejecuta EchoStopper para parar el servidor.");
    }
}
