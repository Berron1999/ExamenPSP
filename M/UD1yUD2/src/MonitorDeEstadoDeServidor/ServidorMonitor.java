package MonitorDeEstadoDeServidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorMonitor {

    static final int PUERTO = 5006;

    // Pares (hilo Java, tarea trabajadora) para poder consultar isAlive() y estado
    static final List<Thread>          hilosJava     = new ArrayList<>();
    static final List<HiloTrabajador>  trabajadores  = new ArrayList<>();

    public static void main(String[] args) {

        // Lanzamos 4 hilos internos con distintas velocidades de trabajo
        lanzarTrabajador("Contador-A", 500);
        lanzarTrabajador("Contador-B", 1000);
        lanzarTrabajador("Contador-C", 300);
        lanzarTrabajador("Contador-D", 800);

        System.out.println("Servidor monitor iniciado en puerto " + PUERTO);
        System.out.println("Hilos internos lanzados: " + trabajadores.size());

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Cliente monitor conectado: " + socketCliente.getInetAddress());
                new Thread(() -> atenderCliente(socketCliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void lanzarTrabajador(String nombre, int pausaMs) {
        HiloTrabajador trabajador = new HiloTrabajador(nombre, pausaMs);
        Thread hilo = new Thread(trabajador, nombre);
        hilosJava.add(hilo);
        trabajadores.add(trabajador);
        hilo.start();
    }

    static void atenderCliente(Socket socket) {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream  in  = new ObjectInputStream(socket.getInputStream())
        ) {
            String peticion;
            while (!(peticion = (String) in.readObject()).equals("SALIR")) {

                if (peticion.equals("ESTADO")) {
                    // Construimos la lista de estados actuales de todos los hilos
                    List<EstadoHilo> estados = new ArrayList<>();
                    for (int i = 0; i < trabajadores.size(); i++) {
                        estados.add(trabajadores.get(i).getEstado(hilosJava.get(i)));
                    }
                    out.reset();
                    out.writeObject(estados);

                } else if (peticion.startsWith("PARAR:")) {
                    // El cliente puede pedir parar un hilo por nombre
                    String nombre = peticion.substring("PARAR:".length());
                    trabajadores.stream()
                            .filter(t -> t.getNombre().equals(nombre))
                            .findFirst()
                            .ifPresent(HiloTrabajador::parar);
                    out.reset();
                    out.writeObject("Hilo " + nombre + " detenido.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Cliente monitor desconectado.");
        }
    }
}