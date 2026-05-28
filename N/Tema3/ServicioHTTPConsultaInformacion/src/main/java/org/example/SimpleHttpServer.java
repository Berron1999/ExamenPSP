package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            // Creamos el servidor en el puerto 8080
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // Requisito: Atender peticiones de forma concurrente
            server.setExecutor(Executors.newFixedThreadPool(10));

            // Definición de rutas (Handlers)
            server.createContext("/", new WelcomeHandler());
            server.createContext("/hora", new TimeHandler());
            server.createContext("/info", new InfoHandler());

            server.start();
            System.out.println("Servidor iniciado en http://localhost:" + PORT);
            System.out.println("Rutas: /, /hora, /info");

        } catch (IOException e) {
            System.err.println("Error iniciando el servidor: " + e.getMessage());
        }
    }

    // Ruta / -> Bienvenida
    static class WelcomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html><body><h1>Bienvenido al Servidor Java</h1><p>Usa /hora o /info</p></body></html>";
            sendResponse(exchange, response, 200);
        }
    }

    // Ruta /hora -> Fecha y hora actual
    static class TimeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            String response = "Fecha y hora actual: " + now;
            sendResponse(exchange, response, 200);
        }
    }

    // Ruta /info -> Nombre equipo y SO
    static class InfoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String pcName = InetAddress.getLocalHost().getHostName();
            String os = System.getProperty("os.name");
            String response = "Equipo: " + pcName + "\nSistema Operativo: " + os;
            sendResponse(exchange, response, 200);
        }
    }

    /**
     * Método auxiliar para enviar respuestas y gestionar códigos de estado
     */
    private static void sendResponse(HttpExchange exchange, String response, int code) throws IOException {
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
