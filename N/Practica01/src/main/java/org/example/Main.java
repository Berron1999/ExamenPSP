package org.example;

import org.example.model.ResultadoFicheros;
import org.example.service.ControladorHilos;
import org.example.service.LectorFichero;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {


        Path carpeta = Path.of("src/main/resources/datos");

        //Num hilos = nucleos de CPU
        int hilos = Runtime.getRuntime().availableProcessors();

        ControladorHilos controladorHilos = new ControladorHilos(hilos);

        List<ResultadoFicheros> resultados = controladorHilos.procesarCarpeta(carpeta);

        controladorHilos.cerrar();

        System.out.println("Total de ficheros procesados: "+resultados.size());

        for (ResultadoFicheros r:resultados){
            System.out.println(r);
        }
    }
}