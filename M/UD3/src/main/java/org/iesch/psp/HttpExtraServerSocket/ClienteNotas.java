package org.iesch.psp.HttpExtraServerSocket;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// Cliente HTTP que consume el servidor de notas usando HttpClient (igual que en los apuntes)
public class ClienteNotas {

    static final String SERVIDOR = "http://localhost:8080";

    // HttpClient estático para toda la aplicación (igual que en los apuntes)
    static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n¿Qué quieres hacer?");
            System.out.println("1. Ver todas las notas (GET /notas)");
            System.out.println("2. Ver número de notas (GET /notas/count)");
            System.out.println("3. Añadir una nota    (POST /notas)");
            System.out.println("4. Salir");
            System.out.print("Opción: ");
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1":
                    // GET /notas → obtiene la lista de notas
                    System.out.println(get(SERVIDOR + "/notas"));
                    break;

                case "2":
                    // GET /notas/count → obtiene el número de notas
                    System.out.println(get(SERVIDOR + "/notas/count"));
                    break;

                case "3":
                    // POST /notas → añade una nueva nota
                    System.out.print("Escribe la nota: ");
                    String textoNota = sc.nextLine().trim();
                    System.out.println(post(SERVIDOR + "/notas", textoNota));
                    break;

                case "4":
                    salir = true;
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // Petición GET → igual que en los apuntes
    private static String get(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("[Cliente] Código HTTP: " + response.statusCode());
            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Error en GET " + url, e);
        }
    }

    // Petición POST → igual que en los apuntes (application/x-www-form-urlencoded)
    private static String post(String url, String nota) {
        try {
            // Codificamos el cuerpo como formulario: "nota=Mi+nueva+nota"
            String cuerpo = "nota=" + URLEncoder.encode(nota, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(cuerpo))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("[Cliente] Código HTTP: " + response.statusCode());
            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Error en POST " + url, e);
        }
    }
}