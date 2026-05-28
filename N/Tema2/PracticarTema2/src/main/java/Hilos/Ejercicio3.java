package Hilos;

public class Ejercicio3 {
    /*⭐ EJERCICIO 3 — Productor / Consumidor con wait-notify (nivel examen real)
Enunciado:
Implementa un buffer de tamaño 1:
producir(int valor)
consumir()
Usa:
wait() para esperar si el buffer está lleno / vacío
notifyAll() para despertar al otro hilo
Y crea:
hilo productor → genera valores del 1 al 5
hilo consumidor → los consume
👉 Este es EL ejercicio estrella del Tema 1.
Con hacerlo una vez más ya lo tienes controlado.*/

    private int valor;
    private boolean disponible=false;

    public synchronized void producir(int i) throws InterruptedException {
        while (disponible){
            wait();
        }
        valor=i;
        disponible=true;
        notifyAll();
    }
    public synchronized int consumir() throws InterruptedException {
        while (!disponible){
            wait();
        }

        disponible=false;
        notifyAll();
        return valor;

    }
}
class P{
    public static void main(String[] args) {
        Ejercicio3 buffer = new Ejercicio3();

        Thread productor = new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                try {
                    buffer.producir(i);
                    System.out.println(Thread.currentThread().getName()+" produce"+i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Hilo-Productor");
        Thread consumidor = new Thread(()->{
            for (int i = 1; i <= 5; i++) {
                try {
                    buffer.consumir();
                    System.out.println(Thread.currentThread().getName()+" consume"+i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Hilo-Consumidor");

        productor.start();
        consumidor.start();
    }
}
