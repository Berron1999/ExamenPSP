package org.example.hilosBasicos;

import java.util.ArrayList;
import java.util.List;

/*🧪 Bloque de ejercicios 1 — Hilos básicos

Sugerencia: crea un proyecto Maven/Gradle o una carpeta con clases sueltas. Cada ejercicio en su propio main.

Ejercicio 1 — “Hola desde N hilos”

Objetivo: crear N hilos que impriman su nombre y terminen.

Tareas:

Crea 10 hilos con Runnable, nómbralos (new Thread(r, "T-"+i)), y haz que impriman “Hola soy …”.

Lanza todos y espera a que terminen con join().*/

public class Ejercicio1 {
    public static void main(String[] args) {
        int n=10;


        //Definimos la tarea que hara cada hilo
        Runnable tarea = () ->{
            String nombre = Thread.currentThread().getName();
            System.out.println("Hola, soy: "+nombre);
        };
        //Crear lista de Thread con nombres H-1 hasta H-n veces.
        List<Thread> hilos = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            Thread hilo = new Thread(tarea,"H-"+i);
            hilos.add(hilo);
        }
        //start() a todos, es decir arrancamos todos los hilos:

        for(Thread h:hilos){
            h.start();
        }

        //join() a todos, esperamos a que acaben todos los hilos:
        for(Thread h:hilos){
            try {
                h.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



        System.out.println("Todos los hilos han terminado");
    }

}
