package ReservaPistasPadel;

public class RegistroPistas {

    private int totalPartidos = 0;
    private int ultimoId = 0;

    // synchronized porque varios partidos pueden terminar casi a la vez
    public synchronized void registrarFinalizacion(int id) {
        totalPartidos++;
        ultimoId = id;
    }

    public synchronized void mostrarResumen() {
        System.out.println("---------------------------------");
        System.out.println("Total de partidos jugados: " + totalPartidos);
        System.out.println("Ultimo partido registrado: " + ultimoId);
    }
}