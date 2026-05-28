package org.example.model;


public class DatosDiarios {
    //Nos creamos el pojo con todo lo que vamos a necesitar para leer los archivos
    private String fecha;
    private String indicativo;
    private String nombre;
    private String provincia;
    private String altitud; // por ahora lo ponemos todo en String para leerlo todo y no nos de problemas al leer el archivo.
    //Mas tarde lo parsearemos todo a nuestro antojo.

    private String tmed;
    private String prec;
    private String tmin;
    private String horatmin;
    private String tmax;
    private String horatmax;

    private String dir;
    private String velmedia;
    private String racha;
    private String horaracha;
    private String sol;
    private String presMax;
    private String horaPresMax;
    private String presMin;
    private String horaPresMin;


    public DatosDiarios() {
    }


    //Me creo solos los getters ya que no vamos a cambiar ningun dato solo los vamos a obtener

    public String getFecha() {
        return fecha;
    }
    public String getIndicativo() {
        return indicativo;
    }
    public String getNombre() {
        return nombre;
    }
    public String getProvincia() {
        return provincia;
    }
    public String getAltitud() {
        return altitud;
    }
    public String getTmed() {
        return tmed;
    }
    public String getPrec() {
        return prec;
    }
    public String getTmin() {
        return tmin;
    }
    public String getHoratmin() {
        return horatmin;
    }
    public String getTmax() {
        return tmax;
    }
    public String getHoratmax() {
        return horatmax;
    }
    public String getDir() {
        return dir;
    }
    public String getVelmedia() {
        return velmedia;
    }
    public String getRacha() {
        return racha;
    }
    public String getHoraracha() {
        return horaracha;
    }
    public String getSol() {
        return sol;
    }
    public String getPresMax() {
        return presMax;
    }
    public String getHoraPresMax() {
        return horaPresMax;
    }
    public String getPresMin() {
        return presMin;
    }
    public String getHoraPresMin() {
        return horaPresMin;
    }

    //Vamos a pasar los Strings a float que necesitemos

    private static Float toFloat(String valor){
        if (valor == null) return null;
        valor = valor.trim().replace(",","."); // Como en el Json estan los float con, los pasamos a . para qque no nos reviente el programa
        if (valor.isEmpty()) return null;

        try {
            return Float.parseFloat(valor);
        }catch (Exception e){
            return null;
        }
    }


    public Float getTmaxFloat() { return toFloat(tmax); }
    public Float getTminFloat() { return toFloat(tmin); }
    public Float getTmedFloat() { return toFloat(tmed); }
    public Float getPrecFloat() { return toFloat(prec); }

    public Float getAltitudFloat() { return toFloat(altitud); }
    public Float getPresMaxFloat() { return toFloat(presMax); }
    public Float getPresMinFloat() { return toFloat(presMin); }
    public Float getVelmediaFloat() { return toFloat(velmedia); }
    public Float getRachaFloat() { return toFloat(racha); }
    public Float getSolFloat() { return toFloat(sol); }


    @Override
    public String toString() {
        return "DatosDiarios{" +
                "fecha='" + fecha + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tmin='" + tmin + '\'' +
                ", tmax='" + tmax + '\'' +
                '}';
    }
}
