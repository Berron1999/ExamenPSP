package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Respuesta {
    // Respuesta exitosa
    public static final int OK = 200;
    // Descripción de la respuesta
    @JsonProperty("descripcion")
    private String descripcion;
    // Estado de la respuesta (200 = OK)
    @JsonProperty("estado")
    private int estado;
    // Enlace a los datos generados (URL)
    @JsonProperty("datos")
    private String datos;
    // Enlace a los metadatos de los datos generados (URL)
    @JsonProperty("metadatos")
    private String metadatos;
    // Getters y setters
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public int getEstado() {
        return estado;
    }
    public void setEstado(int estado) {
        this.estado = estado;
    }
    public String getDatos() {
        return datos;
    }
    public void setDatos(String datos) {
        this.datos = datos;
    }
    public String getMetadatos() {
        return metadatos;
    }
    public void setMetadatos(String metadatos) {
        this.metadatos = metadatos;
    }
    @Override
    public String toString() {
        return "Respuesta{" +
                "descripcion='" + descripcion + '\'' +
                ", estado=" + estado +
                ", datos='" + datos + '\'' +
                ", metadatos='" + metadatos + '\'' +
                '}';
    }
}