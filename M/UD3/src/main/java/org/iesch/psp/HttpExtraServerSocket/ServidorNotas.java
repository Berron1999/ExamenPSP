package org.iesch.psp.HttpExtraServerSocket;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

// Servidor HTTP que gestiona una lista de notas en memoria
public class ServidorNotas {

    static final int PUERTO = 8080;

    // Lista compartida de notas → static para que todos los hilos la compartan
    static final List<String> notas = new ArrayList<>();

    public static void main(String[] args) {
        // Añadimos algunas notas de ejemplo
        notas.add("Estudiar para el examen de PSP");
        notas.add("Entregar ejercicio de FTP");

        System.out.println("[Servidor] Iniciado en http://localhost:" + PUERTO);
        System.out.println("[Servidor] GET  /notas       → lista de notas");
        System.out.println("[Servidor] POST /notas       → añadir una nota");
        System.out.println("[Servidor] GET  /notas/count → número de notas");

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket cliente = serverSocket.accept();
                // Un hilo por petición → servidor concurrente
                new Thread() {
                    public void run() {
                        atenderPeticion(cliente);
                    }
                }.start();
            }
        } catch (IOException e) {
            System.out.println("[Servidor] Error: " + e.getMessage());
        }
    }

    private static void atenderPeticion(Socket cliente) {
        try (
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(cliente.getInputStream()));
                PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)
        ) {
            // Leemos la primera línea: "GET /notas HTTP/1.1"
            String lineaPeticion = entrada.readLine();
            if (lineaPeticion == null || lineaPeticion.isEmpty()) return;

            System.out.println("[Servidor] " + lineaPeticion);

            String[] partes  = lineaPeticion.split(" ");
            String metodo    = partes[0]; // GET o POST
            String ruta      = partes[1]; // /notas, /notas/count...

            // Leemos las cabeceras para obtener Content-Length (necesario en POST)
            int contentLength = 0;
            String linea;
            while (!(linea = entrada.readLine()).isEmpty()) {
                if (linea.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(linea.split(":")[1].trim());
                }
            }

            // Enrutamos según método y ruta
            if (metodo.equals("GET") && ruta.equals("/notas")) {
                // Devuelve la lista de notas en HTML
                StringBuilder html = new StringBuilder();
                html.append("<html><body><h1>Lista de Notas</h1><ul>");
                synchronized (notas) {
                    for (String nota : notas) {
                        html.append("<li>").append(nota).append("</li>");
                    }
                }
                html.append("</ul></body></html>");
                enviarRespuesta(salida, 200, "text/html", html.toString());

            } else if (metodo.equals("GET") && ruta.equals("/notas/count")) {
                // Devuelve el número de notas
                String respuesta = "<html><body><h1>Total de notas: "
                        + notas.size() + "</h1></body></html>";
                enviarRespuesta(salida, 200, "text/html", respuesta);

            } else if (metodo.equals("POST") && ruta.equals("/notas")) {
                // Leemos el cuerpo del POST con el texto de la nueva nota
                char[] cuerpo = new char[contentLength];
                entrada.read(cuerpo, 0, contentLength);
                String cuerpoStr = new String(cuerpo);

                // El cuerpo llega como "nota=Mi+nueva+nota"
                String nuevaNota = cuerpoStr.replace("nota=", "")
                        .replace("+", " ")
                        .trim();

                synchronized (notas) {
                    notas.add(nuevaNota);
                }
                System.out.println("[Servidor] Nota añadida: " + nuevaNota);

                String respuesta = "<html><body><h1>Nota añadida correctamente</h1>"
                        + "<p>" + nuevaNota + "</p>"
                        + "<a href='/notas'>Ver todas las notas</a>"
                        + "</body></html>";
                enviarRespuesta(salida, 200, "text/html", respuesta);

            } else {
                // Ruta no encontrada
                enviarRespuesta(salida, 404, "text/html",
                        "<html><body><h1>404 - No encontrado</h1></body></html>");
            }

        } catch (IOException e) {
            System.out.println("[Servidor] Error: " + e.getMessage());
        }
    }

    // Construye y envía la respuesta HTTP completa
    private static void enviarRespuesta(PrintWriter salida, int codigo,
                                        String tipo, String cuerpo) {
        String estado = codigo == 200 ? "OK" : "Not Found";
        salida.println("HTTP/1.1 " + codigo + " " + estado);
        salida.println("Content-Type: " + tipo + "; charset=UTF-8");
        salida.println("Content-Length: " + cuerpo.getBytes().length);
        salida.println("Connection: close");
        salida.println(); // línea en blanco obligatoria
        salida.println(cuerpo);
        System.out.println("[Servidor] Respuesta: " + codigo + " " + estado);
    }
}