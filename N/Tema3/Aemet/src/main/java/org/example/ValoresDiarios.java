package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
public class ValoresDiarios {
    @JsonProperty("fecha")
    private OffsetDateTime fecha;
    @JsonProperty("indicativo")
    private String indicativo;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("provincia")
    private String provincia;
    @JsonProperty("altitud")
    private long altitud;
    @JsonProperty("tmed")
    private String temperaturaMedia;
    @JsonProperty("prec")
    private String precipitacion;
    @JsonProperty("tmin")
    private String temperaturaMin;
    @JsonProperty("horatmin")
    private String horaTemperaturaMin;
    @JsonProperty("tmax")
    private String temperaturaMax;
    @JsonProperty("horatmax")
    private String horaTemperaturaMax;
    @JsonProperty("dir")
    private String dir;
    @JsonProperty("velmedia")
    private String velocidadMedia;
    @JsonProperty("racha")
    private String racha;
    @JsonProperty("horaracha")
    private String horaRacha;
    @JsonProperty("sol")
    private String sol;
    @JsonProperty("presMax")
    private String presMax;
    @JsonProperty("horaPresMax")
    private String horaPresMax;
    @JsonProperty("presMin")
    private String presMin;
    @JsonProperty("horaPresMin")
    private String horaPresMin;
    // Getters y setters omitidos por brevedad (puedes generarlos con tu IDE)
    @Override
    public String toString() {
        String fechaStr = fecha != null ? fecha.format(DateTimeFormatter.ofPattern("dd / MM / yyyy")) : "N/A";
        return nombre + ", " + altitud + "m (" + provincia + ")\n"
                + fechaStr + "\n"
                + "T Max: " + temperaturaMax + "\n"
                + "T Min: " + temperaturaMin + "\n"
                + "T Media: " + temperaturaMedia + "\n"
                + "Precipitacion: " + precipitacion;
    }
    // Método para deserializar desde JSON a array de ValoresDiarios
    public static ValoresDiarios[] fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, ValoresDiarios[].class);
        } catch (Exception e) {
            throw new RuntimeException("Error parseando JSON", e);
        }
    }
}
