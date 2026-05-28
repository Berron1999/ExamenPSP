package org.example.hilosBasicos;
/*Ejercicio 5 — Condición de carrera (race) y arreglo con synchronized

Objetivo: demostrar un fallo y corregirlo.

Crea Counter con int c.

Lanza 2 hilos: uno incrementa c 100_000 veces; el otro decrementa 100_000.

Sin sincronizar, imprime c al final (no será siempre 0).

Corrige con:

Opción A: synchronized en increment/decrement/get.

Opción B: AtomicInteger.

Pistas: empieza sin sincronización para ver el problema; luego arregla.*/
public class Ejercicio5 {
}
