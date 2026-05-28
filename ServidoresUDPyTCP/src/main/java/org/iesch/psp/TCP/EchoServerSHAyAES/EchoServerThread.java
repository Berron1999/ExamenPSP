package org.iesch.psp.TCP.EchoServerSHAyAES;

import java.io.PrintWriter;
import java.util.Scanner;
import java.net.Socket;
import java.net.ServerSocket;

public class EchoServerThread implements Runnable {
    private Socket cliente;
    private EchoData datos;
    private ServerSocket serverSocket;

    public EchoServerThread(Socket socket, EchoData datos, ServerSocket serverSocket) {
        this.cliente = socket;
        this.datos = datos;
        this.serverSocket = serverSocket;
    }

    public void run() {
        try (Scanner in = new Scanner(cliente.getInputStream());
             PrintWriter out = new PrintWriter(cliente.getOutputStream(), true)) {

            while (in.hasNextLine()) {
                // 1. Leemos el paquete encriptado que llega de la red
                String paqueteCifrado = in.nextLine();

                // 2. Lo desencriptamos para poder leerlo
                String lineaRecibida = CryptoUtil.desencriptar(paqueteCifrado);

                if (lineaRecibida.equals(".")) {
                    out.println(CryptoUtil.encriptar(datos.getEstadisticas()));
                    if (!serverSocket.isClosed()) {
                        serverSocket.close();
                    }
                    break;
                } else if (lineaRecibida.equals("FIN")) {
                    break;
                } else {
                    // 3. Separamos mensaje y hash
                    String[] partes = lineaRecibida.split("\\|");

                    if (partes.length == 2) {
                        String mensajeOriginal = partes[0];
                        String hashRecibido = partes[1];

                        String hashCalculado = HashUtil.getHash(mensajeOriginal);

                        if (hashCalculado.equals(hashRecibido)) {
                            // Todo OK
                            datos.registrarMensaje(mensajeOriginal);

                            // ---- ¡AQUÍ ESTÁ LA MAGIA VISUAL! ----
                            System.out.println("✅ " + mensajeOriginal + " (Hash validado)");
                            // -------------------------------------

                            // Devolvemos el eco (también encriptado, claro)
                            out.println(CryptoUtil.encriptar(mensajeOriginal));
                        } else {
                            System.err.println("❌ ¡ALERTA DE SEGURIDAD! Mensaje corrupto");
                            out.println(CryptoUtil.encriptar("ERROR: Fallo de integridad"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Se interrumpió la conexión con un cliente.");
        }
    }
}