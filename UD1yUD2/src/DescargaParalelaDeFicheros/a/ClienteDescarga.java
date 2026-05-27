package DescargaParalelaDeFicheros.a;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteDescarga {

    static final String HOST           = "localhost";
    static final int    PUERTO         = 5007;
    static final String CARPETA_LOCAL  = "C:\\descargas\\";

    public static void main(String[] args) throws InterruptedException {
        Scanner teclado = new Scanner(System.in);
        new File(CARPETA_LOCAL).mkdirs();

        // ── PASO 1: pedimos al servidor la lista de ficheros disponibles ──
        List<String> disponibles = obtenerListado();
        if (disponibles.isEmpty()) {
            System.out.println("No hay ficheros disponibles en el servidor.");
            return;
        }

        System.out.println("\n--- Ficheros disponibles ---");
        for (int i = 0; i < disponibles.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + disponibles.get(i));
        }

        // ── PASO 2: el usuario elige qué ficheros descargar ──
        System.out.println("\nEscribe los números separados por comas (ej: 1,3): ");
        String[] seleccion = teclado.nextLine().trim().split(",");

        List<String> elegidos = new ArrayList<>();
        for (String s : seleccion) {
            try {
                int indice = Integer.parseInt(s.trim()) - 1;
                if (indice >= 0 && indice < disponibles.size()) {
                    elegidos.add(disponibles.get(indice));
                }
            } catch (NumberFormatException e) {
                System.out.println("Selección inválida: " + s);
            }
        }

        if (elegidos.isEmpty()) {
            System.out.println("No se seleccionó ningún fichero válido.");
            return;
        }

        // ── PASO 3: lanzamos un hilo de descarga por cada fichero elegido ──
        System.out.println("\nDescargando " + elegidos.size() + " fichero(s) en paralelo...");
        long tiempoInicio = System.currentTimeMillis();

        List<Thread> hilos = new ArrayList<>();
        for (String fichero : elegidos) {
            Thread hilo = new Thread(new HiloDescarga(fichero, CARPETA_LOCAL), "Descarga-" + fichero);
            hilos.add(hilo);
            hilo.start(); // cada hilo arranca su propia conexión y descarga independiente
        }

        // ── PASO 4: el main espera a que TODOS los hilos terminen ──
        for (Thread hilo : hilos) {
            hilo.join(); // bloquea el main hasta que este hilo concreto finalice
        }

        long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
        System.out.println("\n✔ Todas las descargas completadas en " + tiempoTotal + " ms.");
        System.out.println("  Ficheros guardados en: " + CARPETA_LOCAL);
    }

    // Conexión aparte solo para pedir el listado
    private static List<String> obtenerListado() {
        List<String> lista = new ArrayList<>();
        try (
                Socket         socket  = new Socket(HOST, PUERTO);
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            salida.println("LISTAR");
            String cabecera = entrada.readLine(); // "LISTA:N"
            int cantidad = Integer.parseInt(cabecera.split(":")[1]);
            for (int i = 0; i < cantidad; i++) lista.add(entrada.readLine());

        } catch (IOException e) {
            System.out.println("Error obteniendo listado: " + e.getMessage());
        }
        return lista;
    }
}