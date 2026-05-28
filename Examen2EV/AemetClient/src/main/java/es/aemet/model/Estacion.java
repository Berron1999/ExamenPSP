package es.aemet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa una estación meteorológica del inventario de AEMET.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Estacion {

    @JsonProperty("latitud")
    private String latitud;

    @JsonProperty("provincia")
    private String provincia;

    @JsonProperty("altitud")
    private String altitud;

    @JsonProperty("indicativo")
    private String indicativo;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("indsinop")
    private String indsinop;

    @JsonProperty("longitud")
    private String longitud;

    // Getters y setters
    public String getLatitud() { return latitud; }
    public void setLatitud(String latitud) { this.latitud = latitud; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getAltitud() { return altitud; }
    public void setAltitud(String altitud) { this.altitud = altitud; }

    public String getIndicativo() { return indicativo; }
    public void setIndicativo(String indicativo) { this.indicativo = indicativo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIndsinop() { return indsinop; }
    public void setIndsinop(String indsinop) { this.indsinop = indsinop; }

    public String getLongitud() { return longitud; }
    public void setLongitud(String longitud) { this.longitud = longitud; }

    @Override
    public String toString() {
        return nombre + " (" + indicativo + ") - " + provincia;
    }
}
