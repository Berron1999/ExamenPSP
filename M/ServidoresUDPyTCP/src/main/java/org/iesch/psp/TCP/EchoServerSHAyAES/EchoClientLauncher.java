package org.iesch.psp.TCP.EchoServerSHAyAES;

//Programa principal (main) que actúa como generador de estrés.
// Su única misión es instanciar 100 EchoClient y lanzarlos a ejecutar en paralelo.
public class EchoClientLauncher {
    public static void main(String[] args) {
        System.out.println("Lanzando 100 clientes simultáneos...");

        for (int i = 1; i <= 100; i++) {
            EchoClient cliente = new EchoClient(i);
            Thread hilo = new Thread(cliente);
            hilo.start();
        }

        System.out.println("Todos los clientes han sido lanzados.");
    }
}