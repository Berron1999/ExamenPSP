package org.example;

import org.example.model.Imagen;

import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Servidor {
    /*a. El servidor deberá atender las peticiones del cliente. Las peticiones del
cliente serán de 2 tipos: Listar imágenes y Enviar imagen.
La primera opción enviará al cliente el listado de imágenes disponibles
(archivos .jpg o .png) en un directorio de trabajo definido en el servidor.

La segunda enviará al cliente el archivo de imagen solicitado (el nombre
del archivo formará parte de la solicitud del cliente).

El servidor debe permitir la conexión de múltiples clientes
simultáneamente.*/

    public static void main(String[] args) {
        int puerto;

        Imagen imagen1 = new Imagen("Paisaje",".jpg");
        Imagen imagen2 = new Imagen("Monte",".jpg");
        Imagen imagen3 = new Imagen("Playa",".jpg");
        List<Imagen> imagenes = new ArrayList<>();

        imagenes.add(imagen1);
        imagenes.add(imagen2);
        imagenes.add(imagen3);

        try{
            puerto = Integer.parseInt(args[0]);
            try(ServerSocket servidor = new ServerSocket(puerto);){

                try(Socket cliente = servidor.accept());
                PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
                Scanner in = new Scanner(cliente.getInputStream());

                {







                }




            } catch (SocketException e) {
                throw new RuntimeException(e);
            }

        }catch (Exception e){
            return;
        }
















    }
}
