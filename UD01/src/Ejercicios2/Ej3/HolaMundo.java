package Ejercicios2.Ej3;

public class HolaMundo {

    // Hilo 1: escribe "Hola " 15 veces cada 2 segundos
    static class HiloHola implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 15; i++) {
                System.out.print("Hola ");

                try {
                    Thread.sleep(2000); // espera 2 segundos entre cada escritura
                } catch (InterruptedException e) {
                    // Si el hilo principal nos interrumpe, salimos limpiamente
                    System.out.println("\n[Hilo 1 interrumpido y finalizado]");
                    return; // finaliza la ejecución del hilo
                }
            }
        }
    }

    // Hilo 2: escribe " mundo!\n" 15 veces cada 2 segundos
    static class HiloMundo implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 15; i++) {
                System.out.println(" mundo!");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("\n[Hilo 2 interrumpido y finalizado]");
                    return;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new HiloHola());
        Thread t2 = new Thread(new HiloMundo());

        t1.start();
        Thread.sleep(20); // pequeño retraso para que t2 arranque justo después de "Hola "
        t2.start();

        // El hilo principal espera 5 segundos y luego interrumpe al hilo 1
        Thread.sleep(5000);
        System.out.println("\n[Hilo principal: interrumpiendo hilo 1...]");
        t1.interrupt();
    }
}