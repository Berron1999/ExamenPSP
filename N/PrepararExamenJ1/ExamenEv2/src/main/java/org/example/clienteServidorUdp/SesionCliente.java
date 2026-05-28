package org.example.clienteServidorUdp;

import java.time.LocalDateTime;

/**
 * Clase que representa una sesión creada por el servidor
 * cada vez que recibe una petición de un cliente UDP.
 */
public class SesionCliente {

    private String identificador;
    private LocalDateTime fechaHoraPeticion;
    private int numeroAleatorio;

    /**
     * Constructor de la sesión del cliente.
     *
     * @param puertoCliente puerto de origen del cliente
     * @param fechaHoraPeticion fecha y hora exactas de la petición
     * @param numeroAleatorio número aleatorio generado entre 1 y 50
     */
    public SesionCliente(int puertoCliente, LocalDateTime fechaHoraPeticion, int numeroAleatorio) {
        this.identificador = "Sesion" + puertoCliente;
        this.fechaHoraPeticion = fechaHoraPeticion;
        this.numeroAleatorio = numeroAleatorio;
    }

    public String getIdentificador() {
        return identificador;
    }

    public LocalDateTime getFechaHoraPeticion() {
        return fechaHoraPeticion;
    }

    public int getNumeroAleatorio() {
        return numeroAleatorio;
    }

    @Override
    public String toString() {
        return "SesionCliente{" +
                "identificador='" + identificador + '\'' +
                ", fechaHoraPeticion=" + fechaHoraPeticion +
                ", numeroAleatorio=" + numeroAleatorio +
                '}';
    }
}