package org.iesch.psp.TCP.VentaDeEntradas;

import java.io.Serializable;

public class PeticionCompra implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idTerminal;
    private int cantidadEntradas;

    public PeticionCompra(String idTerminal, int cantidadEntradas) {
        this.idTerminal = idTerminal;
        this.cantidadEntradas = cantidadEntradas;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public int getCantidadEntradas() {
        return cantidadEntradas;
    }
}