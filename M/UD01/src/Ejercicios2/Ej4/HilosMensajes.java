package Ejercicios2.Ej4;

public class HilosMensajes {

    static class HiloMensajes implements Runnable {
        String[] mensajes = {"Programas", "Procesos", "Servicios", "Hilos"};
        String nombre = Thread.currentThread().getName(); // se actualiza en run()

        @Override
        public void run() {
            nombre = Thread.currentThread().getName();

            for (String mensaje : mensajes) {
                // Si el hilo ha sido interrumpido, imprime sin esperas para terminar rápido
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Hilo: " + nombre + ". " + mensaje);
                    continue;
                }

                System.out.println("Hilo: " + nombre + ". " + mensaje);

                try {
                    Thread.sleep(4000); // espera 4 segundos entre mensaje y mensaje
                } catch (InterruptedException e) {
                    // Nos han interrumpido durante el sleep → marcamos el flag y seguimos sin esperas
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("Hilo: " + nombre + ". *** Finalizado ***");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Leemos el tiempo máximo de espera del argumento del main (en segundos)
        int tiempoMaximo = (args.length > 0) ? Integer.parseInt(args[0]) : 100;
        String nombreMain = Thread.currentThread().getName();

        System.out.println("Hilo: " + nombreMain + ". Tiempo de espera: " + tiempoMaximo + "s");

        // Arrancamos el hilo hijo
        Thread hiloHijo = new Thread(new HiloMensajes());
        hiloHijo.start();

        System.out.println("Hilo: " + nombreMain + ". Esperando a que el hilo " + hiloHijo.getName() + " termine");

        long tiempoInicio = System.currentTimeMillis();
        long tiempoEsperaMs = tiempoMaximo * 1000L;
        boolean cansado = false;

        // El hilo principal espera comprobando cada segundo si el hijo ha terminado
        while (hiloHijo.isAlive()) {
            Thread.sleep(1000); // espera 1 segundo antes de volver a comprobar

            long transcurrido = System.currentTimeMillis() - tiempoInicio;

            if (!cansado && transcurrido >= tiempoEsperaMs) {
                // Se ha superado el tiempo máximo → interrumpimos al hijo
                System.out.println("Hilo: " + nombreMain + ". Cansado de esperar");
                hiloHijo.interrupt();
                cansado = true;
            } else if (!cansado) {
                System.out.println("Hilo: " + nombreMain + ". Todavía esperando...");
            }
        }

        long tiempoTotal = (System.currentTimeMillis() - tiempoInicio) / 1000;
        System.out.println("Hilo: " + nombreMain + ". *** Finalizado. Tiempo de ejecución: " + tiempoTotal + "s. ***");
    }
}