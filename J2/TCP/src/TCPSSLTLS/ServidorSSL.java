package TCPSSLTLS;

import TCPSSLTLS.HiloClienteSSL;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.FileInputStream;
import java.security.KeyStore;

public class ServidorSSL {

    private static final int PUERTO = 6300;

    public static void main(String[] args) {
        try {
            // 1. Cargamos el almacén del servidor con su clave privada y certificado
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("AlmacenServidor.jks"), "PSP_Servidor_2026".toCharArray());

            // 2. KeyManagerFactory: gestiona las claves del servidor para el handshake SSL
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "PSP_Servidor_2026".toCharArray());

            // 3. Creamos el contexto SSL/TLS con las claves del servidor
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            // 4. Creamos el SSLServerSocket en el puerto 6300
            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PUERTO);
            System.out.println("Servidor SSL escuchando en el puerto " + PUERTO);

            // El servidor permanece activo indefinidamente
            while (true) {
                // Esperamos conexión de un cliente → devuelve un SSLSocket
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                System.out.println("Cliente SSL conectado: " + socket.getInetAddress());

                // Lanzamos un hilo para atenderle
                new HiloClienteSSL(socket).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}