package org.example.hilosBasicos;
/*Ejercicio 3 — Interrupción cooperativa

Objetivo: practicar interrupt() y control del ciclo.

Hilo Contador incrementa un número cada 100 ms y lo imprime.

Hilo principal duerme 1,5 s y luego interrupt() al contador.

El contador debe salir limpiamente.

Pistas: atrapa InterruptedException y/o consulta isInterrupted().*/
public class Ejercicio3 {
    public static void main(String[] args) {
        Thread contador = new Thread(new Contador(),"contador");
        contador.start();

        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("(main) esperando...");
        }

        System.out.println("<<<<<<Interrumpiendo el hiloContador>>>>>>");
        contador.interrupt();

        //Esperamos que el hilo termine
        try {
            contador.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hilo principal finaliza");


    }



    static class Contador implements Runnable{

        @Override
        public void run() {
            int i =0;

            try{
                //Bucle infinito , acabará cuando se interrumpa el hilo.
                while (!Thread.currentThread().isInterrupted()){
                    i++;
                    System.out.println("Contador: "+i);
                    Thread.sleep(100);//Se pausa 100 milisegundos
                }
            } catch (InterruptedException e) {
                //Si nos interrumpen mientras dormimos salimos
                System.out.println("Hilo contador interrumpido mientras dormia");
                //restablecemos el flag si queremos detectarlo
                Thread.currentThread().interrupt();
            }
            System.out.println("Hilo contador termina limpiamente.");

        }
    }
}
