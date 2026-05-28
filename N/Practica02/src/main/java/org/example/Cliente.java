package org.example;

import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    /*El cliente debe seleccionar entre tres opciones: Listar imágenes,
Descargar imagen y Salir.
Cuando el usuario elija la primera opción, el cliente conectará con el
servidor para solicitar el listado de imágenes disponibles.
Al elegir la segunda opción el cliente solicitará al usuario el nombre de la
imagen y hará la petición al servidor para recibirla y almacenarla
localmente.
La tercera opción cerrará el cliente.*/
    public static void main(String[] args) {



        try{
            String host = args[0];
            int port = Integer.parseInt(args[0]);

            try(Socket cliente = new Socket(host,port);){
                PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
                Scanner in = new Scanner(cliente.getInputStream());
                Scanner sc = new Scanner(System.in);


            } catch (SocketException e) {
                throw new RuntimeException(e);
            }


        }catch (Exception e){
            return;
        }


    }
}
