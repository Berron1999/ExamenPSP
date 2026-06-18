package SinSemaforo;

import java.util.Random;

public class Cliente extends Thread {

    private int id;
    private Ventanilla[] ventanillas;
    private Random random = new Random();

    public Cliente(int id, Ventanilla[] ventanillas) {
        this.id = id;
        this.ventanillas = ventanillas;
    }

    public void run() {
        try {
            // Espera entre 0 y 1000ms antes de ir al banco
            Thread.sleep(random.nextInt(1001));

            // Elige una ventanilla aleatoriamente
            Ventanilla ventanilla = ventanillas[random.nextInt(ventanillas.length)];

            // Entra en cola
            System.out.println("Cliente " + id + " entra en cola de ventanilla " + ventanilla.getId());

            // Llama a atender() → si hay otro cliente, espera bloqueado hasta que termine
            ventanilla.atender(id);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}