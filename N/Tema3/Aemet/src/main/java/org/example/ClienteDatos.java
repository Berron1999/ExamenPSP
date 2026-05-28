package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClienteDatos {

    // Objeto HttpClient estático para toda la aplicación
    private static final HttpClient cli = HttpClient.newHttpClient();

    /**
     * Devuelve los datos obtenidos de la API REST como String
     *
     * @param uri URL completa del recurso
     * @return contenido de la respuesta
     */
    public static String getDatos(String uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = cli.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo datos de " + uri, e);
        }
    }
}