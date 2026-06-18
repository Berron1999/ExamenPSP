package ReservaPistasPadel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class ClubPadel {

    public static void main(String[] args) {

        // pool de 2 hilos = 2 pistas disponibles como maximo a la vez
        ExecutorService pool = Executors.newFixedThreadPool(2);

        RegistroPistas registro = new RegistroPistas();
        Random random = new Random();

        for (int i = 1; i <= 8; i++) {
            int duracion = 500 + random.nextInt(1501); // entre 500 y 2000 ms
            pool.execute(new Partido(i, duracion, registro));
        }

        // no se aceptan mas tareas, pero se dejan terminar las que ya estan en marcha
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        registro.mostrarResumen();
    }
}