package org.example.hilosBasicos;

public class Ejercicio2 {
    /*Ejercicio 2 — Intercalado y sleep

Objetivo: observar el intercalado de salida.

Crea 2 hilos: uno imprime “A” 20 veces con sleep(50), el otro “B” 20 veces con sleep(50).

Lanza ambos y mira cómo se intercalan.

Reto: cambia los sleep a valores distintos y observa.*/
    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                System.out.println("A");

                try{
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"Hilo-A");


        Thread b = new Thread(()->{
            for (int i = 0; i < 20; i++) {
                System.out.println("B");
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"Hilo-B");

        a.start();
        b.start();

        try{
            a.join();
            b.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Programa terminado");

    }
}
