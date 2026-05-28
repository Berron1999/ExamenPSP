package org.iesch.psp.ej3;

import java.io.Serializable;

public class DatosCliente implements Serializable {
    private static final long serialVersionUID = 1L;

    // Nombre del cliente.
    private final String nombreCliente;
    // Numero aleatorio asignado.
    private final int numero;

    public DatosCliente(String nombreCliente, int numero) {
        this.nombreCliente = nombreCliente;
        this.numero = numero;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public int getNumero() {
        return numero;
    }
}
