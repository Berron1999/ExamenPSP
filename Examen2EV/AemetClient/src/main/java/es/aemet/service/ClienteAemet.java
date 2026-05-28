package es.aemet.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.aemet.model.Estacion;
import es.aemet.model.Respuesta;
import es.aemet.model.ValoresDiarios;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Cliente principal para la API AEMET OpenData.
 * Proporciona métodos para acceder a las 7 funcionalidades de la práctica:
 * 1. Predicción nacional
 * 2. Predicción por comunidades autónomas
 * 3. Predicción por provincias
 * 4. Predicción por localidades (municipios)
 * 5. Predicción por macizos montañosos
 * 6. Predicción por playas
 * 7. Valores climatológicos diarios por estación
 */
public class ClienteAemet {

    private static final String BASE_URL = "https://opendata.aemet.es/opendata";
    private static final String API_KEY_PREFIX = "/?api_key=";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    // Clave API - debe asignarse antes de usar los métodos
    public static String apiKey;

    /**
     * Devuelve el parámetro de la API key para añadir a las URLs
     */
    private static String getApiKeyParam() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("API key no asignada. Asigna ClienteAemet.apiKey antes de hacer peticiones.");
        }
        return API_KEY_PREFIX + apiKey;
    }

    /**
     * Realiza una petición GET a la API y devuelve el objeto Respuesta
     */
    private static Respuesta hacerPeticion(String uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("cache-control", "no-cache")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error HTTP: " + response.statusCode() + " - " + response.body());
            }

            return mapper.readValue(response.body(), Respuesta.class);
        } catch (Exception e) {
            throw new RuntimeException("Error en petición a " + uri, e);
        }
    }

    /**
     * Obtiene los datos como texto plano desde la URL de datos de la respuesta
     */
    private static String obtenerDatosTexto(Respuesta resp) {
        if (resp.getEstado() != Respuesta.OK) {
            throw new RuntimeException("Error API: " + resp.getEstado() + " - " + resp.getDescripcion());
        }
        return ClienteDatos.getDatos(resp.getDatos());
    }

    // =====================================================================
    // 1. PREDICCIÓN NACIONAL
    // =====================================================================

    /**
     * Predicción general para España hoy (texto plano)
     * GET /api/prediccion/nacional/hoy
     */
    public static String prediccionNacionalHoy() {
        String uri = BASE_URL + "/api/prediccion/nacional/hoy" + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Predicción general para España mañana (texto plano)
     * GET /api/prediccion/nacional/manana
     */
    public static String prediccionNacionalManana() {
        String uri = BASE_URL + "/api/prediccion/nacional/manana" + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Predicción general para España pasado mañana (texto plano)
     * GET /api/prediccion/nacional/pasadomanana
     */
    public static String prediccionNacionalPasadoManana() {
        String uri = BASE_URL + "/api/prediccion/nacional/pasadomanana" + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Predicción general para España a medio plazo (texto plano)
     * GET /api/prediccion/nacional/medioplazo
     */
    public static String prediccionNacionalMedioPlazo() {
        String uri = BASE_URL + "/api/prediccion/nacional/medioplazo" + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Predicción general para España tendencia (texto plano)
     * GET /api/prediccion/nacional/tendencia
     */
    public static String prediccionNacionalTendencia() {
        String uri = BASE_URL + "/api/prediccion/nacional/tendencia" + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    // =====================================================================
    // 2. PREDICCIÓN POR COMUNIDADES AUTÓNOMAS
    // =====================================================================

    /**
     * Predicción por comunidad autónoma hoy (texto plano)
     * GET /api/prediccion/ccaa/hoy/{ccaa}
     *
     * @param ccaa código de la comunidad autónoma (ej: "val" para Valencia)
     */
    public static String prediccionCCAAHoy(String ccaa) {
        String uri = BASE_URL + "/api/prediccion/ccaa/hoy/" + ccaa + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Predicción por comunidad autónoma mañana (texto plano)
     * GET /api/prediccion/ccaa/manana/{ccaa}
     */
    public static String prediccionCCAAManana(String ccaa) {
        String uri = BASE_URL + "/api/prediccion/ccaa/manana/" + ccaa + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Predicción por comunidad autónoma pasado mañana (texto plano)
     * GET /api/prediccion/ccaa/pasadomanana/{ccaa}
     */
    public static String prediccionCCAAPasadoManana(String ccaa) {
        String uri = BASE_URL + "/api/prediccion/ccaa/pasadomanana/" + ccaa + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Predicción por comunidad autónoma medio plazo (texto plano)
     * GET /api/prediccion/ccaa/medioplazo/{ccaa}
     */
    public static String prediccionCCAAMedioPlazo(String ccaa) {
        String uri = BASE_URL + "/api/prediccion/ccaa/medioplazo/" + ccaa + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    // =====================================================================
    // 3. PREDICCIÓN POR PROVINCIAS
    // =====================================================================

    /**
     * Predicción por provincia hoy (texto plano)
     * GET /api/prediccion/provincia/hoy/{provincia}
     *
     * @param provincia código de provincia (ej: "46" para Valencia)
     */
    public static String prediccionProvinciaHoy(String provincia) {
        String uri = BASE_URL + "/api/prediccion/provincia/hoy/" + provincia + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Predicción por provincia mañana (texto plano)
     * GET /api/prediccion/provincia/manana/{provincia}
     */
    public static String prediccionProvinciaManana(String provincia) {
        String uri = BASE_URL + "/api/prediccion/provincia/manana/" + provincia + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    // =====================================================================
    // 4. PREDICCIÓN POR LOCALIDADES (MUNICIPIOS)
    // =====================================================================

    /**
     * Predicción diaria por municipio (JSON)
     * GET /api/prediccion/especifica/municipio/diaria/{municipio}
     *
     * @param municipio código INE del municipio (5 dígitos, ej: "46250" para Valencia)
     */
    public static String prediccionMunicipioDiaria(String municipio) {
        String uri = BASE_URL + "/api/prediccion/especifica/municipio/diaria/" + municipio + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Predicción horaria por municipio (JSON)
     * GET /api/prediccion/especifica/municipio/horaria/{municipio}
     *
     * @param municipio código INE del municipio (5 dígitos)
     */
    public static String prediccionMunicipioHoraria(String municipio) {
        String uri = BASE_URL + "/api/prediccion/especifica/municipio/horaria/" + municipio + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    // =====================================================================
    // 5. PREDICCIÓN POR MACIZOS MONTAÑOSOS
    // =====================================================================

    /**
     * Predicción de montaña para un área y día concretos (texto plano)
     * GET /api/prediccion/especifica/montaña/pasada/area/{area}/dia/{dia}
     *
     * Códigos de área:
     *   peu1 - Picos de Europa
     *   nav1 - Pirineo Navarro
     *   arn1 - Pirineo Aragonés
     *   cat1 - Pirineo Catalán
     *   rio1 - Ibérica Riojana
     *   arn2 - Ibérica Aragonesa
     *   mad2 - Sierras de Guadarrama y Somosierra
     *   gre1 - Sierra de Gredos
     *   nev1 - Sierra Nevada
     *
     * @param area código del macizo montañoso
     * @param dia  día de la predicción (0=hoy, 1=mañana, 2=pasado, 3=en 3 días)
     */
    public static String prediccionMontana(String area, int dia) {
        String uri = BASE_URL + "/api/prediccion/especifica/monta%C3%B1a/pasada/area/"
                + area + "/dia/" + dia + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    /**
     * Información nivológica para un área montañosa (texto plano)
     * GET /api/prediccion/especifica/nivologica/{area}
     */
    public static String informacionNivologica(String area) {
        String uri = BASE_URL + "/api/prediccion/especifica/nivologica/" + area + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    // =====================================================================
    // 6. PREDICCIÓN POR PLAYAS
    // =====================================================================

    /**
     * Predicción para una playa concreta (JSON)
     * GET /api/prediccion/especifica/playa/{playa}
     *
     * Los códigos de playa se pueden consultar en:
     * http://www.aemet.es/documentos/es/eltiempo/prediccion/playas/Playas_codigos.csv
     *
     * @param playa código de la playa
     */
    public static String prediccionPlaya(String playa) {
        String uri = BASE_URL + "/api/prediccion/especifica/playa/" + playa + getApiKeyParam();
        Respuesta resp = hacerPeticion(uri);
        return obtenerDatosTexto(resp);
    }

    // =====================================================================
    // 7. VALORES CLIMATOLÓGICOS DIARIOS POR ESTACIÓN (ya desarrollado en clase)
    // =====================================================================

    /**
     * Obtiene el inventario completo de estaciones meteorológicas
     * GET /api/valores/climatologicos/inventarioestaciones/todasestaciones
     */
    public static List<Estacion> inventarioEstacionesTodas() {
        try {
            String uri = BASE_URL + "/api/valores/climatologicos/inventarioestaciones/todasestaciones"
                    + getApiKeyParam();
            Respuesta resp = hacerPeticion(uri);
            String datosJson = obtenerDatosTexto(resp);
            return mapper.readValue(datosJson, new TypeReference<List<Estacion>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo inventario de estaciones", e);
        }
    }

    /**
     * Obtiene los valores climatológicos diarios de una estación
     * GET /api/valores/climatologicos/diarios/datos/fechaini/{fechaIni}/fechafin/{fechaFin}/estacion/{idema}
     *
     * @param fechaIni fecha inicio en formato AAAA-MM-DDTHH:MM:SSUTC
     * @param fechaFin fecha fin en formato AAAA-MM-DDTHH:MM:SSUTC
     * @param idema    identificador de la estación
     */
    public static ValoresDiarios[] valoresClimaDiarios(String fechaIni, String fechaFin, String idema) {
        try {
            String uri = BASE_URL + "/api/valores/climatologicos/diarios/datos"
                    + "/fechaini/" + fechaIni
                    + "/fechafin/" + fechaFin
                    + "/estacion/" + idema
                    + getApiKeyParam();
            Respuesta resp = hacerPeticion(uri);
            String datosJson = obtenerDatosTexto(resp);
            return ValoresDiarios.fromJson(datosJson);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo valores diarios", e);
        }
    }
}
