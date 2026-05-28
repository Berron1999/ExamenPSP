package ClienteServidorFicheros;

import java.io.*;
import java.net.Socket;

// Cada instancia de esta clase atiende a un cliente en su propio hilo
public class HiloCliente implements Runnable {

    private Socket socket;

    public HiloCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida    = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String peticion;
            // Leemos peticiones del cliente hasta que cierre la conexión
            while ((peticion = entrada.readLine()) != null) {

                if (peticion.equals("LISTAR")) {
                    procesarListar(salida);

                } else if (peticion.startsWith("MOSTRAR:")) {
                    // Extraemos el nombre del fichero de la petición
                    String nombreFichero = peticion.substring("MOSTRAR:".length());
                    procesarMostrar(salida, nombreFichero);

                } else if (peticion.equals("SALIR")) {
                    System.out.println("Cliente desconectado: " + socket.getInetAddress());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error con cliente: " + e.getMessage());
        }
    }

    // Envía al cliente la lista de ficheros del directorio
    private void procesarListar(PrintWriter salida) {
        File directorio = new File(Servidor.DIRECTORIO);
        File[] ficheros = directorio.listFiles(File::isFile);

        if (ficheros == null || ficheros.length == 0) {
            salida.println("No hay ficheros disponibles.");
        } else {
            for (File f : ficheros) {
                salida.println(f.getName());
            }
        }
        // Línea especial que indica al cliente que el listado ha terminado
        salida.println("FIN");
    }

    // Envía al cliente el contenido del fichero solicitado
    private void procesarMostrar(PrintWriter salida, String nombreFichero) {
        File fichero = new File(Servidor.DIRECTORIO + nombreFichero);

        if (!fichero.exists() || !fichero.isFile()) {
            salida.println("ERROR: Fichero no encontrado.");
            salida.println("FIN");
            return;
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                salida.println(linea);
            }
        } catch (IOException e) {
            salida.println("ERROR al leer el fichero.");
        }
        // Línea especial que indica al cliente que el contenido ha terminado
        salida.println("FIN");
    }
}