package org.iesch.psp.Practica0Ej2http;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServidorHTTP {

    static final int PUERTO = 8080;

    public static void main(String[] args) {
        System.out.println("[Servidor HTTP] Escuchando en puerto " + PUERTO);
        System.out.println("[Servidor HTTP] Rutas disponibles:");
        System.out.println("  http://localhost:" + PUERTO + "/");
        System.out.println("  http://localhost:" + PUERTO + "/hora");
        System.out.println("  http://localhost:" + PUERTO + "/info");

        // ServerSocket acepta conexiones HTTP entrantes, igual que con cualquier TCP
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                // accept() bloquea hasta que llega una petición HTTP
                Socket cliente = serverSocket.accept();
                System.out.println("\n[Servidor HTTP] Petición de: "
                        + cliente.getInetAddress());

                // Un hilo por petición → servidor concurrente
                new Thread() {
                    public void run() {
                        atenderPeticion(cliente);
                    }
                }.start();
            }
        } catch (IOException e) {
            System.out.println("[Servidor HTTP] Error: " + e.getMessage());
        }
    }

    private static void atenderPeticion(Socket cliente) {
        try (
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(cliente.getInputStream()));
                PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)
        ) {
            // Leemos la primera línea de la petición HTTP: "GET /ruta HTTP/1.1"
            String lineaPeticion = entrada.readLine();
            if (lineaPeticion == null) return;

            System.out.println("[Servidor HTTP] " + lineaPeticion);

            // Extraemos la ruta: "GET /hora HTTP/1.1" → "/hora"
            String[] partes = lineaPeticion.split(" ");
            String metodo = partes[0];  // GET, POST...
            String ruta   = partes[1];  // /, /hora, /info...

            // Solo atendemos GET
            if (!metodo.equals("GET")) {
                enviarRespuesta(salida, 405, "text/plain", "Método no permitido");
                return;
            }

            // Enrutamos según la URL solicitada
            switch (ruta) {
                case "/":
                    enviarRespuesta(salida, 200, "text/html", paginaBienvenida());
                    break;
                case "/hora":
                    enviarRespuesta(salida, 200, "text/html", paginaHora());
                    break;
                case "/info":
                    enviarRespuesta(salida, 200, "text/html", paginaInfo());
                    break;
                default:
                    // Ruta no encontrada → 404
                    enviarRespuesta(salida, 404, "text/html", pagina404(ruta));
            }

        } catch (IOException e) {
            System.out.println("[Servidor HTTP] Error atendiendo cliente: "
                    + e.getMessage());
        }
    }

    // Construye y envía la respuesta HTTP completa con cabeceras y cuerpo
    private static void enviarRespuesta(PrintWriter salida, int codigo,
                                        String tipo, String cuerpo) {
        // Línea de estado HTTP
        String estado = codigo == 200 ? "OK"
                : codigo == 404 ? "Not Found"
                  : "Method Not Allowed";

        salida.println("HTTP/1.1 " + codigo + " " + estado);
        salida.println("Content-Type: " + tipo + "; charset=UTF-8");
        salida.println("Content-Length: " + cuerpo.getBytes().length);
        salida.println("Connection: close");
        salida.println(); // línea en blanco obligatoria entre cabeceras y cuerpo
        salida.println(cuerpo);

        System.out.println("[Servidor HTTP] Respuesta enviada: " + codigo + " " + estado);
    }

    // Ruta / → página de bienvenida en HTML
    private static String paginaBienvenida() {
        return "<html><body>"
                + "<h1>Bienvenido al Servidor HTTP</h1>"
                + "<p>Rutas disponibles:</p>"
                + "<ul>"
                + "<li><a href='/hora'>/hora</a> - Fecha y hora actual</li>"
                + "<li><a href='/info'>/info</a> - Información del sistema</li>"
                + "</ul>"
                + "</body></html>";
    }

    // Ruta /hora → fecha y hora actual del sistema
    private static String paginaHora() {
        String horaActual = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        return "<html><body>"
                + "<h1>Fecha y hora actual</h1>"
                + "<p>" + horaActual + "</p>"
                + "<a href='/'>Volver</a>"
                + "</body></html>";
    }

    // Ruta /info → información del sistema operativo y equipo
    private static String paginaInfo() {
        String nombreEquipo  = "Desconocido";
        try {
            nombreEquipo = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // si no se puede obtener dejamos el valor por defecto
        }
        String sistemaOp = System.getProperty("os.name");
        String version   = System.getProperty("os.version");
        String java      = System.getProperty("java.version");

        return "<html><body>"
                + "<h1>Información del sistema</h1>"
                + "<ul>"
                + "<li><b>Equipo:</b> " + nombreEquipo + "</li>"
                + "<li><b>Sistema operativo:</b> " + sistemaOp + "</li>"
                + "<li><b>Versión SO:</b> " + version + "</li>"
                + "<li><b>Java:</b> " + java + "</li>"
                + "</ul>"
                + "<a href='/'>Volver</a>"
                + "</body></html>";
    }

    // Ruta no encontrada → 404
    private static String pagina404(String ruta) {
        return "<html><body>"
                + "<h1>404 - Página no encontrada</h1>"
                + "<p>La ruta <b>" + ruta + "</b> no existe.</p>"
                + "<a href='/'>Volver al inicio</a>"
                + "</body></html>";
    }
}