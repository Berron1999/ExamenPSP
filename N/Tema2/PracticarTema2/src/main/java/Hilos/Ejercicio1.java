package Hilos;

public class Ejercicio1 {
    /*Enunciado estilo examen:
Crea un hilo que imprima números del 1 al 20 cada 300 ms.
A los 2 segundos, el main debe interrumpirlo.
El hilo debe detectar la interrupción y terminar limpiamente mostrando un mensaje:
"Hilo detenido".
👉 Hazlo tú en 3–5 minutos.
Si quieres, me pasas tu código y te lo corrijo al instante.*/

    public static void main(String[] args) throws InterruptedException {


        Thread h1 = new Thread(()->{
            for (int i = 1; i <=20 ; i++) {
                System.out.println(i);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    System.out.println("Hilo detenido");
                    break;
                }
            }
        });
        h1.start();
        Thread.sleep(2000);

        h1.interrupt();
        h1.join();
        System.out.println(Thread.currentThread().getName()+" Proceso terminado");

    }
}
