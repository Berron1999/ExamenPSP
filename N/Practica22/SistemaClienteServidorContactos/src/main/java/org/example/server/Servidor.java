package org.example.server;

import org.example.model.Contacto;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    /*Desarrolla un sistema cliente servidor.
a. El servidor deberá atender las peticiones del cliente. Las peticiones del
cliente serán de 2 tipos: Listar contactos y Buscar contacto.
La primera opción enviará al cliente el listado completo de contactos
almacenados (nombre, teléfono, correo).*/
    public static void main(String[] args) {

        try{
            ServerSocket servidor = new ServerSocket(5000);
            System.out.println("Servidor esperando a cliente.");

            while (true){
                Socket socket = servidor.accept();
                System.out.println("Cliente aceptado");

                new Thread((new ManejadorDeHilos(socket))).start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/*1. Desarrolla un sistema cliente servidor.
a. El servidor deberá atender las peticiones del cliente. Las peticiones del
cliente serán de 2 tipos: Listar contactos y Buscar contacto.
La primera opción enviará al cliente el listado completo de contactos
almacenados (nombre, teléfono, correo).
La segunda enviará al cliente la información de un contacto específico
cuyo nombre formará parte de la solicitud del cliente.
El servidor debe permitir la conexión de múltiples clientes.
2. b. El cliente debe seleccionar entre tres opciones: Listar contactos, Buscar
contacto y Salir.
Cuando el usuario elija la primera opción, el cliente conectará con el
servidor para solicitar el listado de contactos.


Al elegir la segunda opción el cliente solicitará al usuario el nombre del
contacto, hará la petición al servidor y mostrará los datos recibidos al
usuario.
La tercera opción cerrará el cliente.*/
