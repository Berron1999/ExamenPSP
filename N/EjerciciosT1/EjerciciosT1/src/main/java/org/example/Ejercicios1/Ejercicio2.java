package org.example.Ejercicios1;

import java.io.File;
import java.io.IOException;

public class Ejercicio2 {
    /*2. Hemos visto en los ejemplos que podemos lanzar un proceso con
ProcessBuilder y ejecutar comandos de consola (cmd) en él. En el ejemplo
indicábamos exactamente el comando a ejecutar y mostrábamos por consola
el resultado de la ejecución. La clase ProcessBuilder permite redirigir la
entrada, salida y los errores en la ejecución de proceso a ficheros. Busca
información en la ayuda de Java sobre los métodos redirectInput,
redirectOutput y redirectError. Deberás desarrollar un programa que lance un
subproceso cmd con ProcessBuilder, el programa debe obtener los comandos
a ejecutar por la consola de un fichero .bat que habrás creado tú
previamente. El programa dejará el log de ejecución en un fichero de salida y
el log de errores en otro fichero. Deberás utilizar los métodos redirectInput,
redirectOutput y redirectError.
El fichero bat podría tener por ejemplo los siguientes comandos:
ping www.dam2chomon.org
ping www.google.es
pring www.iesch.org
De esa forma veremos que ocurre en cada situación:
• un comando correcto con una dirección que no existe
• un comando correcto con una dirección que existe
• un comando incorrecto*/
    public static void main(String[] args) {
        File archivoEntrada = new File("Ejercicio2.bat");

        File archivoSalida = new File("salida.log");
        File archivoError = new File("error.log");

        try{
            ProcessBuilder proceso = new ProcessBuilder("cmd.exe");

            proceso.redirectInput(archivoEntrada);
            proceso.redirectOutput(archivoSalida);
            proceso.redirectError(archivoError);

            System.out.println("Ejecutando Proceso");

            Process process = proceso.start();
            int estadoSalida = process.waitFor();

            System.out.println("Proceso finalizado con codigo de salida: " + estadoSalida);
            System.out.println("Resultados en 'salida.log' y errores en 'error.log'");


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
