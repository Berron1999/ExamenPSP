package TransferenciasBancariasSeguras;

import java.io.Serializable;

public class DatosTransferencia implements Serializable {

    private String cuentaOrigen;
    private String cuentaDestino;
    private double importe;
    private String hmacEnviado;

    public DatosTransferencia(String cuentaOrigen, String cuentaDestino, double importe, String hmacEnviado) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.importe = importe;
        this.hmacEnviado = hmacEnviado;
    }

    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }

    public double getImporte() {
        return importe;
    }

    public String getHmacEnviado() {
        return hmacEnviado;
    }
}