package UDP.AlertasMetereologicas;

import java.io.Serializable;

public class DatosAlerta implements Serializable {

    private String tipo;
    private int nivel;

    public DatosAlerta(String tipo, int nivel) {
        this.tipo = tipo;
        this.nivel = nivel;
    }

    public String getTipo() {
        return tipo;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return "Alerta{tipo='" + tipo + "', nivel=" + nivel + "}";
    }
}