package org.example.clienteServidorUDP2EjercicioChat;

import java.time.LocalDateTime;

public class OperacionCliente {

    private String identificador;
    private LocalDateTime fechaHoraPeticion;
    private double numeroRecibido;
    private double resultadoCalculado;

    public OperacionCliente(int puertoCliente, LocalDateTime fechaHoraPeticion,
                            double numeroRecibido, double resultadoCalculado)
    {

        this.identificador = "Operacion" + puertoCliente;
        this.fechaHoraPeticion = fechaHoraPeticion;
        this.numeroRecibido = numeroRecibido;
        this.resultadoCalculado = resultadoCalculado;
    }

    public String getIdentificador() {
        return identificador;
    }

    public LocalDateTime getFechaHoraPeticion() {
        return fechaHoraPeticion;
    }

    public double getNumeroRecibido() {
        return numeroRecibido;
    }

    public double getResultadoCalculado() {
        return resultadoCalculado;
    }

    @Override
    public String toString() {
        return "OperacionCliente{" +
                "identificador='" + identificador + '\'' +
                ", fechaHoraPeticion=" + fechaHoraPeticion +
                ", numeroRecibido=" + numeroRecibido +
                ", resultadoCalculado=" + resultadoCalculado +
                '}';
    }
}