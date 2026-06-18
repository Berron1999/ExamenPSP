package ReservaPistasPadel;

public class Partido implements Runnable {

    private final int id;
    private final int duracion;
    private final RegistroPistas registro;

    public Partido(int id, int duracion, RegistroPistas registro) {
        this.id = id;
        this.duracion = duracion;
        this.registro = registro;
    }

    @Override
    public void run() {
        System.out.println("Partido " + id + " empieza a jugarse (" + duracion + " ms)");
        try {
            Thread.sleep(duracion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Partido " + id + " ha terminado");

        registro.registrarFinalizacion(id);
    }
}