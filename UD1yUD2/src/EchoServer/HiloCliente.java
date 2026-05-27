package EchoServer;

import java.io.*;
import java.net.Socket;

// Hilo que simula UN cliente: envía 100 mensajes y verifica que el eco es correcto
public class HiloCliente implements Runnable {

    private int idCliente;

    public HiloCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public void run() {
        try (
                Socket         socket  = new Socket(EchoCliente.HOST, EchoCliente.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[Cliente-" + idCliente + "] Conectado.");

            for (int i = 1; i <= EchoCliente.MENSAJES_POR_CLIENTE; i++) {
                String msg = "Cliente-" + idCliente + "-Msg-" + i;

                // Enviamos el mensaje y actualizamos el contador compartido
                salida.println(msg);
                EchoCliente.sumarEnviado(); // synchronized

                // Recibimos el eco y comprobamos que coincide con lo enviado
                String eco = entrada.readLine();
                if (msg.equals(eco)) {
                    EchoCliente.sumarCorrecto(); // synchronized
                }

                // Mostramos cada 25 mensajes para no saturar la consola
                if (i % 25 == 0) {
                    System.out.println("[Cliente-" + idCliente + "] "
                            + i + " mensajes. Eco correcto: " + msg.equals(eco));
                }
            }
            System.out.println("[Cliente-" + idCliente + "] Finalizado.");

        } catch (IOException e) {
            System.out.println("[Cliente-" + idCliente + "] Error: " + e.getMessage());
        }
    }
}