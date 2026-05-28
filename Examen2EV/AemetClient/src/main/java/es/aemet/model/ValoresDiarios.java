package es.aemet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Representa los valores climatológicos diarios de una estación.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValoresDiarios {

    @JsonProperty("fecha")
    private String fecha;

    @JsonProperty("indicativo")
    private String indicativo;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("provincia")
    private String provincia;

    @JsonProperty("altitud")
    private String altitud;

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

    // Getters y setters
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getIndicativo() { return indicativo; }
    public void setIndicativo(String indicativo) { this.indicativo = indicativo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getAltitud() { return altitud; }
    public void setAltitud(String altitud) { this.altitud = altitud; }

    public String getTemperaturaMedia() { return temperaturaMedia; }
    public void setTemperaturaMedia(String temperaturaMedia) { this.temperaturaMedia = temperaturaMedia; }

    public String getPrecipitacion() { return precipitacion; }
    public void setPrecipitacion(String precipitacion) { this.precipitacion = precipitacion; }

    public String getTemperaturaMin() { return temperaturaMin; }
    public void setTemperaturaMin(String temperaturaMin) { this.temperaturaMin = temperaturaMin; }

    public String getHoraTemperaturaMin() { return horaTemperaturaMin; }
    public void setHoraTemperaturaMin(String horaTemperaturaMin) { this.horaTemperaturaMin = horaTemperaturaMin; }

    public String getTemperaturaMax() { return temperaturaMax; }
    public void setTemperaturaMax(String temperaturaMax) { this.temperaturaMax = temperaturaMax; }

    public String getHoraTemperaturaMax() { return horaTemperaturaMax; }
    public void setHoraTemperaturaMax(String horaTemperaturaMax) { this.horaTemperaturaMax = horaTemperaturaMax; }

    public String getDir() { return dir; }
    public void setDir(String dir) { this.dir = dir; }

    public String getVelocidadMedia() { return velocidadMedia; }
    public void setVelocidadMedia(String velocidadMedia) { this.velocidadMedia = velocidadMedia; }

    public String getRacha() { return racha; }
    public void setRacha(String racha) { this.racha = racha; }

    public String getHoraRacha() { return horaRacha; }
    public void setHoraRacha(String horaRacha) { this.horaRacha = horaRacha; }

    public String getSol() { return sol; }
    public void setSol(String sol) { this.sol = sol; }

    public String getPresMax() { return presMax; }
    public void setPresMax(String presMax) { this.presMax = presMax; }

    public String getHoraPresMax() { return horaPresMax; }
    public void setHoraPresMax(String horaPresMax) { this.horaPresMax = horaPresMax; }

    public String getPresMin() { return presMin; }
    public void setPresMin(String presMin) { this.presMin = presMin; }

    public String getHoraPresMin() { return horaPresMin; }
    public void setHoraPresMin(String horaPresMin) { this.horaPresMin = horaPresMin; }

    @Override
    public String toString() {
        return nombre + ", " + altitud + "m (" + provincia + ")\n"
                + fecha + "\n"
                + "T Max: " + temperaturaMax + " | T Min: " + temperaturaMin
                + " | T Media: " + temperaturaMedia + "\n"
                + "Precipitación: " + precipitacion;
    }

    /**
     * Deserializa un JSON a un array de ValoresDiarios
     */
    public static ValoresDiarios[] fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, ValoresDiarios[].class);
        } catch (Exception e) {
            throw new RuntimeException("Error parseando JSON de ValoresDiarios", e);
        }
    }
}
