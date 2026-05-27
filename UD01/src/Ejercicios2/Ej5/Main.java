package Ejercicios2.Ej5;

import Ejercicios2.Ej5.Saldo;
import Ejercicios2.Ej5.TareaDeposito;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // Creamos el objeto compartido con saldo inicial de 1000
        Saldo saldo = new Saldo(1000);
        System.out.println("Saldo inicial: " + saldo.getSaldo());

        // Creamos varios hilos que comparten el mismo objeto Saldo
        Thread t1 = new Thread(new TareaDeposito(saldo, 200), "Hilo-A");
        Thread t2 = new Thread(new TareaDeposito(saldo, 350), "Hilo-B");
        Thread t3 = new Thread(new TareaDeposito(saldo, 150), "Hilo-C");
        Thread t4 = new Thread(new TareaDeposito(saldo, 500), "Hilo-D");

        // Lanzamos todos los hilos
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // Esperamos a que todos los hilos terminen antes de mostrar el saldo final
        t1.join();
        t2.join();
        t3.join();
        t4.join();

        // Saldo esperado: 1000 + 200 + 350 + 150 + 500 = 2200
        System.out.println("Saldo final: " + saldo.getSaldo());
    }
}