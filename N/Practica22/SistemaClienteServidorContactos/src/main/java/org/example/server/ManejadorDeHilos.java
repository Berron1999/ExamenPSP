package org.example.server;

import org.example.model.Contacto;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ManejadorDeHilos implements Runnable {
    Socket socket;

    //Nos creamos el constructor
    public ManejadorDeHilos(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {

        List<Contacto> listaDeContactos = new ArrayList<>(List.of(
                new Contacto("Pablo",123334455,"pnavarroc@iesch.org"),
                new Contacto("Mario",987776655,"mario@iesch.org"),
                new Contacto("David",666778844,"david@iesch.org"),
                new Contacto("Sara",767889965,"sara@iesch.org"),
                new Contacto("Mama",765665544,"mama@iesch.org"),
                new Contacto("Diego",989776655,"diego@iesch.org")
        ));

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

            String linea;
            while ((linea=br.readLine())!=null){

                if (linea.startsWith("LISTAR")){
                    for (Contacto con : listaDeContactos){
                        pw.println(con.getNombre());
                    }
                    pw.println("FIN");


                }else if (linea.startsWith("BUSCAR")){
                        String nombreBuscado=linea.substring(7).trim();

                        Contacto cont = listaDeContactos.stream().filter(c->c.getNombre().equals(nombreBuscado)).findFirst().orElse(null);

                        pw.println(cont.toString());

                }else if (linea.equalsIgnoreCase("SALIR")){
                    pw.println("Conexion cerrada");
                    break;
                }else pw.println("Comando invalido");

            }
            socket.close();
            System.out.printf("Cliente desconectado");
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
