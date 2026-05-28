package org.iesch.psp.TCP.VentaDeEntradas;

import java.net.ServerSocket;
import java.net.Socket;

public class ServidorVentas {
    public static void main(String[] args) {
        int puerto = 9000;

        // 1. Creamos el almacén compartido (UNA SOLA VEZ)
        InventarioTeatro inventario = new InventarioTeatro();

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor de Taquillas iniciado. Esperando clientes...");

            // 2. Bucle infinito del recepcionista
            while (true) {
                // Aceptamos al cliente que acaba de llegar
                Socket cliente = servidor.accept();
                System.out.println("Nuevo cliente conectado desde: " + cliente.getInetAddress());

                // 3. Creamos un trabajador (Hilo) y le damos el cliente y el inventario
                ServidorVentasThread trabajador = new ServidorVentasThread(cliente, inventario);

                // 4. ¡Lo arrancamos en paralelo!
                new Thread(trabajador).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}