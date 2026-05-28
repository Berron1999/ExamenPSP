package ejercicio1;

import java.io.*;
import java.net.Socket;

public class ManejadorCliente implements Runnable {

    private Socket socket;
    public ManejadorCliente(Socket socket) {
        this.socket= socket;
    }


    @Override
    public void run() {

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);


            String linea;

            while ((linea = br.readLine())!=null){

                if (linea.equalsIgnoreCase("Listar")){
                    File carpeta = new File(Servidor.ruta);
                    String[] ficheros = carpeta.list();
                    pw.println(String.join(",",ficheros));

                }else if (linea.startsWith("MOSTRAR")){
                    String[] partes = linea.split(" ",2);
                    if (partes.length<2){
                        pw.println("Error debes indicar el nombre del fichero");
                        continue;
                    }
                    File archivo = new File(Servidor.ruta+"/"+partes[1]);

                    if (!archivo.exists()){
                        pw.println("El archivo indicado no existe");
                    }else {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo));
                        String contenido="";
                        String l ;
                        while ((l=bufferedReader.readLine())!=null){
                            contenido += l;
                            contenido += l;
                        }
                        bufferedReader.close();
                        pw.println(contenido);
                    }


                }else if (linea.equalsIgnoreCase("SALIR")){
                    pw.println("Conexion cerrada.");
                    break;
                }else {
                    pw.println("Comando invalido");
                }

            }

            socket.close();
            System.out.printf("Cliente desconectado");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
