package Hilos;

public class Ejercicio2 {
    /*Enunciado:
Implementa una clase Contador con un valor entero.
Tiene un metodo sincronizado incrementar() que suma 1 al contador.
Crea dos hilos que incrementen el contador 1000 veces cada uno.
El valor final del contador debe ser 2000.
👉 Este ejercicio cae mucho.
Puedes hacerlo rápido porque ya lo dominas.*/
    public static void main(String[] args) throws InterruptedException {
        Contador contador = new Contador();
        Thread h1 = new Thread(()->{
            for (int i = 0; i < 1000; i++) {
                contador.incrementar();
            }
        });
        Thread h2 = new Thread(()->{
            for (int i = 0; i < 1000; i++) {
                contador.incrementar();
            }
        });
        h1.start();
        h2.start();
        h1.join();
        h2.join();
        System.out.println(Thread.currentThread().getName()+" : El valor final es:"+contador.getValor());
    }
}
class Contador{
    private int valor=0;

    public synchronized void incrementar(){
        valor++;
    }

    public int getValor() {
        return valor;
    }
}