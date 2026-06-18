package TCP.ReservaMesas;
public class GestorMesas {

    private boolean[] ocupadas = new boolean[5]; // indices 0 a 4 -> mesas 1 a 5

    // synchronized para evitar que dos clientes reserven la misma mesa a la vez
    public synchronized boolean reservar(int numeroMesa) {
        if (numeroMesa < 1 || numeroMesa > 5) {
            return false;
        }

        int indice = numeroMesa - 1;
        if (ocupadas[indice]) {
            return false;
        }

        ocupadas[indice] = true;
        return true;
    }
}