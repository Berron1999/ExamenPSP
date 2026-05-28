package org.iesch.psp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HiloCliente extends Thread {
    private final Socket socket;

    public HiloCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream())) {
            // Espera el objeto DatosBusqueda del cliente.
            Object recibido = entrada.readObject();
            if (!(recibido instanceof DatosBusqueda)) {
                // Respuesta de error si el objeto no es el esperado.
                salida.writeInt(-1);
                salida.flush();
                return;
            }

            DatosBusqueda datos = (DatosBusqueda) recibido;
            // Descarga el contenido y cuenta ocurrencias.
            int ocurrencias = contarOcurrencias(datos.getUrl(), datos.getCadena());
            salida.writeInt(ocurrencias);
            salida.flush();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en HiloCliente: " + e.getMessage());
        } finally {
            // Cierra el socket del cliente.
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar socket: " + e.getMessage());
            }
        }
    }

    private int contarOcurrencias(String urlTexto, String cadena) throws IOException {
        if (cadena == null || cadena.isEmpty()) {
            return 0;
        }

        // Lee el contenido de la URL linea a linea.
        URL url = new URL(urlTexto);
        int total = 0;
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                // Suma las ocurrencias en cada linea.
                total += contarEnLinea(linea, cadena);
            }
        }
        return total;
    }

    private int contarEnLinea(String linea, String cadena) {
        int total = 0;
        int indice = 0;
        while (true) {
            // Busca coincidencias sin solapamiento.
            indice = linea.indexOf(cadena, indice);
            if (indice < 0) {
                break;
            }
            total++;
            indice += cadena.length();
        }
        return total;
    }
}
