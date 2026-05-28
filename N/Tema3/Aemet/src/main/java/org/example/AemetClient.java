package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class AemetClient {
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwbmF2YXJyb2NAaWVzY2gub3JnIiwianRpIjoiMjlmNzg3ODctZmQ3Ni00MmFlLTk3ZTctY2ZkYjQwMTgzMTg3IiwiaXNzIjoiQUVNRVQiLCJpYXQiOjE3Njk3MTYzMjMsInVzZXJJZCI6IjI5Zjc4Nzg3LWZkNzYtNDJhZS05N2U3LWNmZGI0MDE4MzE4NyIsInJvbGUiOiIifQ.Nzz3-y2cViCEoLWbSS1Z8JNhikBzhNy1iXKopebzwkU";
    private static final String URL =
            "https://opendata.aemet.es/opendata/api/valores/climatologicos/inventarioestaciones/todasestaciones/?api_key=" + API_KEY;
    public static void main(String[] args) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("cache-control", "no-cache")
                    .GET().build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println("Código HTTP: " + response.statusCode());
            System.out.println("Cuerpo de la respuesta:");
            System.out.println(response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
