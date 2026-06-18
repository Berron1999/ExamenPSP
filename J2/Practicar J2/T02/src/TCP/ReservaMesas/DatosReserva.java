package TCP.ReservaMesas;

import java.io.Serializable;

public class DatosReserva implements Serializable {

    private int numeroMesa;

    public DatosReserva(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }
}