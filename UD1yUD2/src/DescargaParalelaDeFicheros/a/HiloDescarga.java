package DescargaParalelaDeFicheros.a;

import java.io.*;
import java.net.Socket;

// Cada instancia de esta clase descarga UN fichero en su propio hilo
public class HiloDescarga implements Runnable {

    private String nombreFichero;
    private String carpetaDestino;

    public HiloDescarga(String nombreFichero, String carpetaDestino) {
        this.nombreFichero  = nombreFichero;
        this.carpetaDestino = carpetaDestino;
    }

    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] Iniciando descarga: " + nombreFichero);

        try (
                Socket         socket  = new Socket(ClienteDescarga.HOST, ClienteDescarga.PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Cada hilo abre su propia conexión independiente al servidor
            salida.println("DESCARGAR:" + nombreFichero);

            File destino = new File(carpetaDestino + nombreFichero);
            try (PrintWriter escritor = new PrintWriter(new FileWriter(destino))) {
                String linea;
                while ((linea = entrada.readLine()) != null && !linea.equals("FIN")) {
                    if (linea.equals("ERROR")) {
                        System.out.println("[" + Thread.currentThread().getName()
                                + "] Error: fichero no encontrado en servidor.");
                        return;
                    }
                    escritor.println(linea);
                }
            }

            System.out.println("[" + Thread.currentThread().getName()
                    + "] Descarga completada: " + destino.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("[" + Thread.currentThread().getName()
                    + "] Error descargando " + nombreFichero + ": " + e.getMessage());
        }
    }
}