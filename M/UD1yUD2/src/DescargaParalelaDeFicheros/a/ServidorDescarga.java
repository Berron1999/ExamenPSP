package DescargaParalelaDeFicheros.a;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorDescarga {

    static final int    PUERTO     = 5007;
    static final String DIRECTORIO = "C:\\servidor_ficheros\\";

    public static void main(String[] args) {
        System.out.println("Servidor de descargas iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socketCliente = serverSocket.accept();
                // Cada petición de descarga se atiende en su propio hilo
                new Thread(() -> atenderCliente(socketCliente)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void atenderCliente(Socket socket) {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String peticion = entrada.readLine();

            if (peticion.equals("LISTAR")) {
                // Enviamos lista de ficheros disponibles
                File directorio = new File(DIRECTORIO);
                File[] ficheros = directorio.listFiles(File::isFile);
                if (ficheros == null || ficheros.length == 0) {
                    salida.println("LISTA:0");
                } else {
                    salida.println("LISTA:" + ficheros.length);
                    for (File f : ficheros) salida.println(f.getName());
                }

            } else if (peticion.startsWith("DESCARGAR:")) {
                // Enviamos el contenido del fichero solicitado
                String nombre  = peticion.substring("DESCARGAR:".length());
                File   fichero = new File(DIRECTORIO + nombre);

                if (!fichero.exists()) {
                    salida.println("ERROR");
                    salida.println("FIN");
                    return;
                }

                try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
                    String linea;
                    while ((linea = lector.readLine()) != null) salida.println(linea);
                }
                salida.println("FIN");
                System.out.println("[Servidor] Fichero enviado: " + nombre);
            }

        } catch (IOException e) {
            System.out.println("Error atendiendo cliente: " + e.getMessage());
        }
    }
}