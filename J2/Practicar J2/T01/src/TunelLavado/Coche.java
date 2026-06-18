package TunelLavado;

import java.util.concurrent.Semaphore;

// Esta clase representa un coche, y cada coche se va a ejecutar como un hilo independiente
// Por eso extendemos Thread (en vez de implementar Runnable), siguiendo el patrón que usamos siempre
public class Coche extends Thread {

    // Identificador del coche, simplemente para diferenciarlo en los mensajes por consola
    private final int id;

    // Tipo de lavado que ha pedido este coche: "basico", "completo" o "premium"
    private final String tipo;

    // Semáforo COMPARTIDO entre todos los coches, representa las 3 bahías de lavado disponibles
    // Lo recibimos por constructor en lugar de crearlo aquí, porque tiene que ser el MISMO objeto
    // para todos los coches (si cada coche creara su propio semáforo, no estarían compartiendo las bahías)
    private final Semaphore bahias;

    // Contador COMPARTIDO para saber cuántos coches se han lavado en total
    // Igual que el semáforo, debe ser una única instancia compartida por todos los hilos
    private final ContadorLavados contador;

    // Constructor: recibe todo lo que el coche necesita para funcionar
    public Coche(int id, String tipo, Semaphore bahias, ContadorLavados contador) {
        this.id = id;
        this.tipo = tipo;
        this.bahias = bahias;
        this.contador = contador;
    }

    // Método run(): el código que se ejecuta cuando el hilo arranca con start()
    @Override
    public void run() {
        try {
            // Pedimos permiso al semáforo para entrar a una bahía
            // Si las 3 bahías están ocupadas, el hilo se queda BLOQUEADO aquí hasta que se libere una
            bahias.acquire();

            // En este punto, ya tenemos una bahía asignada, así que avisamos por consola
            System.out.println("Coche " + id + " entra a lavarse (" + tipo + ")");

            // Calculamos el tiempo de lavado según el tipo, usando un método auxiliar de la propia clase
            int tiempoLavado = calcularTiempoLavado();

            // Simulamos el tiempo que tarda el lavado durmiendo el hilo
            Thread.sleep(tiempoLavado);

            // Avisamos de que el coche ha terminado y sale del túnel
            System.out.println("Coche " + id + " sale del túnel (" + tipo + ")");

            // Incrementamos el contador global de coches lavados de forma segura
            contador.incrementar();

        } catch (InterruptedException e) {
            // Si el hilo es interrumpido mientras espera o duerme, lo mostramos por consola
            e.printStackTrace();
        } finally {
            // IMPORTANTE: liberamos la bahía SIEMPRE, tanto si todo ha ido bien como si ha habido excepción
            // Si no liberáramos aquí, una excepción podría dejar la bahía bloqueada para siempre
            bahias.release();
        }
    }

    // Método auxiliar privado que devuelve el tiempo de lavado en milisegundos según el tipo
    private int calcularTiempoLavado() {
        if (tipo.equals("basico")) {
            return 1000;
        } else if (tipo.equals("completo")) {
            return 2000;
        } else {
            // Si no es básico ni completo, asumimos que es premium
            return 3000;
        }
    }
}