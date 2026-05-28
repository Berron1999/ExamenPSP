package org.iesch.psp.UDP.UDPSerializar;

import java.io.Serializable;

// Implementamos Serializable para poder enviar el objeto por el DatagramSocket
public class DatosCliente implements Serializable {

    private static final long serialVersionUID = 1L; // Buena práctica al serializar
    private String nombreCliente;
    private int numero;

    // Constructor
    public DatosCliente(String nombreCliente, int numero) {
        this.nombreCliente = nombreCliente;
        this.numero = numero;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}