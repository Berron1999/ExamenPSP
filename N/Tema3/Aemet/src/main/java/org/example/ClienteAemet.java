package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
public class ClienteAemet {
    private static final String DIR = "https://opendata.aemet.es/opendata";
    private static final String API_KEY_PARAM_PREFIX = "/?api_key=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwbmF2YXJyb2NAaWVzY2gub3JnIiwianRpIjoiMjlmNzg3ODctZmQ3Ni00MmFlLTk3ZTctY2ZkYjQwMTgzMTg3IiwiaXNzIjoiQUVNRVQiLCJpYXQiOjE3Njk3MTYzMjMsInVzZXJJZCI6IjI5Zjc4Nzg3LWZkNzYtNDJhZS05N2U3LWNmZGI0MDE4MzE4NyIsInJvbGUiOiIifQ.Nzz3-y2cViCEoLWbSS1Z8JNhikBzhNy1iXKopebzwkU";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    // Clave API que se debe asignar antes de hacer peticiones
    public static String apiKey;
    private static String getApiKeyParam() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("API key no asignada");
        }
        return API_KEY_PARAM_PREFIX + apiKey;
    }
    // Obtiene todas las estaciones
    public static List<Estacion> inventarioEstacionesTodas() {
        try {
            String uri = DIR + "/api/valores/climatologicos/inventarioestaciones/todasestaciones" + getApiKeyParam();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("cache-control", "no-cache")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Error HTTP: " + response.statusCode());
            }
            // Parseamos la respuesta de la API
            Respuesta resp = mapper.readValue(response.body(), Respuesta.class);
            if (resp.getEstado() != Respuesta.OK) {
                throw new RuntimeException("Error API: " + resp.getEstado() + " - " + resp.getDescripcion());
            }
            // Obtenemos los datos reales desde la URL que devuelve la API
            String datosJson = ClienteDatos.getDatos(resp.getDatos());
            // Convertimos JSON a lista de Estacion
            return mapper.readValue(datosJson, new TypeReference<List<Estacion>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo inventario de estaciones", e);
        }
    }
            // Obtiene los valores diarios de una estación
    public static ValoresDiarios[] valoresClimaDiarios(String fechaIni, String fechaFin, String idema) {
        try {
            String uri = DIR + "/api/valores/climatologicos/diarios/datos"
                    + "/fechaini/" + fechaIni
                    + "/fechafin/" + fechaFin
                    + "/estacion/" + idema
                    + getApiKeyParam();


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("cache-control", "no-cache")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Error HTTP: " + response.statusCode());
            }
            // Parseamos la respuesta de la API
            Respuesta resp = mapper.readValue(response.body(), Respuesta.class);
            if (resp.getEstado() != Respuesta.OK) {
                throw new RuntimeException("Error API: " + resp.getEstado() + " - " + resp.getDescripcion());
            }
            // Obtenemos los datos reales desde la URL que devuelve la API
            String datosJson = ClienteDatos.getDatos(resp.getDatos());
            // Convertimos JSON a array de ValoresDiarios
            return ValoresDiarios.fromJson(datosJson);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo valores diarios", e);
        }
    }
}