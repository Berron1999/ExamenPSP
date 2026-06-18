package ConSemaforo;

import java.util.Random;

public class Cliente extends Thread {

    private int id;
    private Ventanilla[] ventanillas; // Array de ventanillas disponibles
    private Random random = new Random();

    public Cliente(int id, Ventanilla[] ventanillas) {
        this.id = id;
        this.ventanillas = ventanillas;
    }

    public void run() {
        try {
            // 1. Espera entre 0 y 1000ms antes de ir al banco
            Thread.sleep(random.nextInt(1001));

            // 2. Elige una ventanilla aleatoriamente
            Ventanilla ventanilla = ventanillas[random.nextInt(ventanillas.length)];

            // 3. Entra en la cola de esa ventanilla
            System.out.println("Cliente " + id + " entra en cola de ventanilla " + ventanilla.getId());
            ventanilla.adquirir(); // Si hay otro cliente siendo atendido, espera aquí

            // 4. Está siendo atendido
            System.out.println("Cliente " + id + " siendo atendido en ventanilla " + ventanilla.getId());
            Thread.sleep(500 + random.nextInt(1001)); // Entre 500 y 1500ms de atención

            // 5. Realiza una operación entre 50€ y 1000€
            double operacion = 50 + random.nextInt(951); // nextInt(951) da 0-950, + 50 = 50-1000
            ventanilla.registrarAtencion(operacion);

            // 6. Abandona la ventanilla
            System.out.println("Cliente " + id + " abandona ventanilla " + ventanilla.getId() + " (operación: " + operacion + "€)");
            ventanilla.liberar(); // Libera la ventanilla para el siguiente cliente

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}