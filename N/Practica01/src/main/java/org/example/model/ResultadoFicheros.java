package org.example.model;

public class ResultadoFicheros {
    //Con esta clase vamos a recopilar todos los datos que vamos a necesitar, lo que nos piden

    //---MAX---
    private Float tmax;
    private String fechaMax;
    private String horaMax;
    private String estacionMax;
    //---MIN---
    private Float tmin;
    private String fechaMin;
    private String horaMin;
    private String estacionMin;

    //--GETTERS--

    public Float getTmax() {
        return tmax;
    }

    public String getFechaMax() {
        return fechaMax;
    }

    public String getHoraMax() {
        return horaMax;
    }

    public String getEstacionMax() {
        return estacionMax;
    }

    public Float getTmin() {
        return tmin;
    }

    public String getFechaMin() {
        return fechaMin;
    }

    public String getHoraMin() {
        return horaMin;
    }

    public String getEstacionMin() {
        return estacionMin;
    }

    //--SETTERS--MAX--

    public void setMax(Float tmax, String fecha, String hora, String estacion) {
        this.tmax = tmax;
        this.fechaMax = fecha;
        this.horaMax = hora;
        this.estacionMax = estacion;
    }

    //--SETTERS--MIN--

    public void setMin(Float tmin, String fecha, String hora, String estacion) {
        this.tmin = tmin;
        this.fechaMin = fecha;
        this.horaMin = hora;
        this.estacionMin = estacion;
    }

    @Override
    public String toString() {
        return "Max: " + tmax + "ºC en " + estacionMax + " (" + fechaMax + " " + horaMax + ")\n" +
                "Min: " + tmin + "ºC en " + estacionMin + " (" + fechaMin + " " + horaMin + ")";
    }

}
