package Ejercicios2.Ej5;

public class Saldo {

    private double saldo;

    public Saldo(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    // Getter público con sleep aleatorio (simula latencia de acceso)
    public double getSaldo() {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return saldo;
    }

    // Setter privado con sleep aleatorio
    private void setSaldo(double saldo) {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.saldo = saldo;
    }

    // synchronized → solo un hilo puede ejecutar este método a la vez (exclusión mutua)
    // Si quitamos synchronized, varios hilos pueden leer y escribir a la vez → saldo incorrecto
    public synchronized void añadir(String quien, double cantidad) {
        double saldoAntes = getSaldo();
        double saldoDespues = saldoAntes + cantidad;

        System.out.println("[" + quien + "] Añadiendo: " + cantidad
                + " | Saldo antes: " + saldoAntes
                + " | Saldo después: " + saldoDespues);

        setSaldo(saldoDespues);
    }
}