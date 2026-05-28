package org.example.service;

import org.example.model.ResultadoFicheros;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ControladorHilos {

    private final ExecutorService pool; //Esto es una interfaz de java y es un gestor de hilos pre-creados que trabajan en paralelo.
    private final LectorFichero lector = new LectorFichero();


    public ControladorHilos(int numHilos) {
        this.pool = Executors.newFixedThreadPool(numHilos);
    }

    public List<ResultadoFicheros> procesarCarpeta(Path carpeta) throws ExecutionException, InterruptedException {

        List<Future<ResultadoFicheros>> futures = new ArrayList<>();

        //Recorremos todos los ficheros , ahora tenemos solo uno pero imaginemos que tuvieramos cientos de archivos con datos meteorologicos
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(carpeta)) {

            for (Path p : stream) {
                //Enviamos cada fichero a un hilo para que lo lea
                Future<ResultadoFicheros> futuro = pool.submit(() -> lector.procesar(p));

                futures.add(futuro);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Recogemos todos los resultados de los hilos
        List<ResultadoFicheros> resultadoFicheros = new ArrayList<>();

        for (Future<ResultadoFicheros> f : futures) {
            ResultadoFicheros r = f.get(); //Esperamos a que el hilo termine
            if (r != null) {
                resultadoFicheros.add(r);
            }


        }

        return resultadoFicheros;


    }

    public void cerrar() {
        pool.shutdown();
    }
}



