package es.aemet.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase auxiliar para obtener los datos y metadatos desde las URLs
 * que devuelve la API AEMET en sus respuestas.
 */
public class ClienteDatos {

    private static final HttpClient cli = HttpClient.newHttpClient();

    /**
     * Realiza una petición GET a la URI proporcionada y devuelve el cuerpo como String.
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
