package TCPSSLTLS;

import TCPSSLTLS.DatosMensaje;
import TCPSSLTLS.RespuestaServidor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ClienteSSL {

    private static final String HOST = "localhost";
    private static final int PUERTO = 6300;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // 1. Cargamos el almacén del cliente que contiene el certificado del servidor
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(new FileInputStream("AlmacenCliente.jks"), "PSP_Cliente_2026".toCharArray());

            // 2. TrustManagerFactory: le dice al cliente en qué certificados confía
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);

            // 3. Creamos el contexto SSL/TLS con los certificados de confianza
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // Pedimos los datos al usuario
            System.out.print("Introduce tu nombre de usuario: ");
            String nombreUsuario = scanner.nextLine();
            System.out.print("Introduce tu mensaje: ");
            String mensaje = scanner.nextLine();

            // 4. Creamos el SSLSocket y conectamos al servidor
            SSLSocketFactory sf = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) sf.createSocket(HOST, PUERTO);

            // Streams — igual que en TCP normal
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Enviamos el objeto DatosMensaje
            DatosMensaje datos = new DatosMensaje(nombreUsuario, mensaje, LocalDateTime.now());
            out.reset();
            out.writeObject(datos);

            // Recibimos y mostramos la respuesta
            RespuestaServidor respuesta = (RespuestaServidor) in.readObject();
            System.out.println("\n--- Respuesta del servidor SSL ---");
            System.out.println("Hash SHA-256: " + respuesta.getHashGenerado());
            System.out.println("Longitud del mensaje: " + respuesta.getNumeroCaracteresMensaje() + " caracteres");
            System.out.println("Fecha de recepción: " + respuesta.getFechaRecepcion());

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}