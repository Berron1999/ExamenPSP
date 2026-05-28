package org.iesch.psp.UDP.UDPMonitoreo;

import java.io.Serializable;

public class MetricaSistema implements Serializable {

    // Buena práctica siempre que usemos Serializable
    private static final long serialVersionUID = 1L;

    private String nombreEquipo;
    private int usoCPU;
    private int usoRAM;

    // Constructor
    public MetricaSistema(String nombreEquipo, int usoCPU, int usoRAM) {
        this.nombreEquipo = nombreEquipo;
        this.usoCPU = usoCPU;
        this.usoRAM = usoRAM;
    }

    // Getters para leer los datos cuando lleguen al servidor
    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public int getUsoCPU() {
        return usoCPU;
    }

    public int getUsoRAM() {
        return usoRAM;
    }
}