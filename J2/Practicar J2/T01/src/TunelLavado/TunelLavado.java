package TunelLavado;

import java.util.concurrent.Semaphore;
import java.util.Random;

// Clase principal: aquí montamos toda la simulación y la lanzamos desde el main
public class TunelLavado {

    public static void main(String[] args) {

        // Creamos el semáforo con 3 permisos: representa las 3 bahías de lavado disponibles
        // Este MISMO objeto se pasará a todos los coches para que compartan las bahías
        Semaphore bahias = new Semaphore(3);

        // Creamos UNA SOLA instancia del contador, que también compartirán todos los coches
        ContadorLavados contador = new ContadorLavados();

        // Array donde guardamos los tipos de lavado posibles, para elegir uno al azar por coche
        String[] tipos = {"basico", "completo", "premium"};

        // Random para elegir el tipo de lavado de cada coche
        Random random = new Random();

        // Array de hilos donde guardamos los 10 coches que vamos a crear
        // Lo necesitamos para poder hacer join() de todos ellos después
        Coche[] coches = new Coche[10];

        // Bucle para crear y lanzar los 10 coches
        for (int i = 0; i < 10; i++) {
            // Elegimos un tipo de lavado aleatorio del array de tipos
            String tipoElegido = tipos[random.nextInt(tipos.length)];

            // Creamos el coche, pasándole su id (i+1 para que empiece en 1, no en 0),
            // el tipo elegido, y las referencias COMPARTIDAS al semáforo y al contador
            coches[i] = new Coche(i + 1, tipoElegido, bahias, contador);

            // Arrancamos el hilo, lo que hace que se ejecute su método run() en paralelo
            coches[i].start();
        }

        // Ahora esperamos a que TODOS los coches terminen antes de seguir
        // join() bloquea el hilo principal hasta que el hilo correspondiente termine
        try {
            for (int i = 0; i < coches.length; i++) {
                coches[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Una vez que todos los coches han terminado (y por tanto han incrementado el contador),
        // podemos mostrar el resultado final con total seguridad de que el valor es correcto
        System.out.println("---------------------------------");
        System.out.println("Total de coches lavados: " + contador.getTotal());
    }
}