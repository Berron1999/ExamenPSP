package org.iesch.psp.TCP.VentaDeEntradas;

import org.iesch.psp.TCP.VentaDeEntradas.InventarioTeatro;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServidorVentasThread implements Runnable {
    private Socket socket;
    private InventarioTeatro inventario;

    public ServidorVentasThread(Socket socket, InventarioTeatro inventario) {
        this.socket = socket;
        this.inventario = inventario;
    }

    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // 1. Leemos el objeto y lo transformamos DIRECTAMENTE, sin instanceof
            PeticionCompra peticion = (PeticionCompra) in.readObject();

            // 2. Evaluamos qué quiere hacer basándonos en el ID del terminal
            if (peticion.getIdTerminal().equals("INFO")) {

                // Es el administrador preguntando el aforo
                out.writeObject("Quedan " + inventario.getEntradasDisponibles() + " entradas disponibles.");

            } else {

                // Es un cliente normal intentando comprar
                System.out.println("Terminal " + peticion.getIdTerminal() + " solicita " + peticion.getCantidadEntradas() + " entradas.");

                boolean exito = inventario.venderEntradas(peticion.getCantidadEntradas());

                if (exito) {
                    out.writeObject("COMPRA OK. Entradas asignadas al terminal " + peticion.getIdTerminal());
                } else {
                    out.writeObject("ERROR: Entradas insuficientes para el terminal " + peticion.getIdTerminal());
                }
            }

        } catch (Exception e) {
            System.err.println("Error procesando al cliente: " + e.getMessage());
        }
    }
}