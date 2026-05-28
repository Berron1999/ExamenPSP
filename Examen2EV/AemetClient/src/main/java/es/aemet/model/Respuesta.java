package es.aemet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa la respuesta estándar de la API AEMET OpenData.
 * Todas las peticiones devuelven esta estructura con enlaces a datos y metadatos.
 */
public class Respuesta {

    public static final int OK = 200;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("estado")
    private int estado;

    @JsonProperty("datos")
    private String datos;

    @JsonProperty("metadatos")
    private String metadatos;

    // Getters y setters
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public String getDatos() { return datos; }
    public void setDatos(String datos) { this.datos = datos; }

    public String getMetadatos() { return metadatos; }
    public void setMetadatos(String metadatos) { this.metadatos = metadatos; }

    @Override
    public String toString() {
        return "Respuesta{descripcion='" + descripcion + "', estado=" + estado +
               ", datos='" + datos + "', metadatos='" + metadatos + "'}";
    }
}
