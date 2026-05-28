package org.iesch.psp.Practica0Ej1FTP;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.cert.X509Certificate;
import java.util.Scanner;

public class ClienteFTP {

    public static void main(String[] args) {

        // Usamos FTPSClient igual que en los apuntes (FTP sobre TLS)
        FTPSClient ftp = new FTPSClient("TLS", false);
        Scanner sc = new Scanner(System.in);

        try {
            // Aceptamos certificado autofirmado (como en los apuntes)
            ftp.setTrustManager(new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) {}
                public void checkServerTrusted(X509Certificate[] xcs, String string) {}
                public X509Certificate[] getAcceptedIssuers() { return null; }
            });

            // Pedimos los datos de conexión al usuario
            System.out.print("Servidor FTP: ");
            String servidor = sc.nextLine().trim();

            System.out.print("Usuario: ");
            String usuario = sc.nextLine().trim();

            System.out.print("Contraseña: ");
            String contrasena = sc.nextLine().trim();

            // Conectamos al servidor
            System.out.println("[FTP] Conectando a " + servidor + "...");
            ftp.connect(servidor);

            // Comprobamos que la conexión fue exitosa
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                System.out.println("[FTP] Error: no se pudo conectar al servidor.");
                ftp.disconnect();
                return;
            }
            System.out.println("[FTP] Conectado. Código: " + ftp.getReplyCode());

            // Login con usuario y contraseña
            boolean loginOk = ftp.login(usuario, contrasena);
            if (!loginOk) {
                System.out.println("[FTP] Error: usuario o contraseña incorrectos.");
                ftp.disconnect();
                return;
            }
            System.out.println("[FTP] Login correcto. " + ftp.getReplyString());

            // Configuración de seguridad y modo pasivo (igual que en los apuntes)
            ftp.execPBSZ(0);
            ftp.execPROT("P");           // protección de datos
            ftp.enterLocalPassiveMode(); // modo pasivo para evitar problemas con firewalls

            // Mostramos el contenido del directorio actual
            System.out.println("\n[FTP] Contenido del directorio actual:");
            mostrarDirectorio(ftp);

            // Menú de opciones
            boolean salir = false;
            while (!salir) {
                System.out.println("\n¿Qué quieres hacer?");
                System.out.println("1. Subir un archivo");
                System.out.println("2. Descargar un archivo");
                System.out.println("3. Salir");
                System.out.print("Opción: ");
                String opcion = sc.nextLine().trim();

                switch (opcion) {
                    case "1":
                        // Subir archivo al servidor
                        System.out.print("Ruta local del archivo a subir: ");
                        String rutaSubir = sc.nextLine().trim();
                        subirArchivo(ftp, rutaSubir);
                        break;

                    case "2":
                        // Descargar archivo del servidor
                        System.out.print("Nombre del archivo a descargar: ");
                        String nombreDescargar = sc.nextLine().trim();
                        System.out.print("Ruta local donde guardarlo: ");
                        String rutaDescargar = sc.nextLine().trim();
                        descargarArchivo(ftp, nombreDescargar, rutaDescargar);
                        break;

                    case "3":
                        salir = true;
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            }

            // Cerramos sesión y desconectamos (igual que en los apuntes)
            ftp.logout();
            ftp.disconnect();
            System.out.println("[FTP] Desconectado correctamente.");

        } catch (IOException e) {
            System.out.println("[FTP] Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Muestra el contenido del directorio actual (igual que en los apuntes)
    private static void mostrarDirectorio(FTPSClient ftp) throws IOException {
        FTPFile[] files = ftp.listFiles();
        if (files.length == 0) {
            System.out.println("  (directorio vacío)");
        }
        for (FTPFile file : files) {
            // Mostramos tipo (DIR o FILE) y nombre
            String tipo = file.isDirectory() ? "[DIR] " : "[FILE]";
            System.out.println("  " + tipo + " " + file.getName());
        }
    }

    // Sube un archivo local al servidor
    private static void subirArchivo(FTPSClient ftp, String rutaLocal) throws IOException {
        File archivo = new File(rutaLocal);
        if (!archivo.exists()) {
            System.out.println("[FTP] Error: el archivo local no existe.");
            return;
        }
        try (FileInputStream fis = new FileInputStream(archivo)) {
            // storeFile sube el archivo al directorio actual del servidor
            boolean ok = ftp.storeFile(archivo.getName(), fis);
            if (ok) {
                System.out.println("[FTP] Archivo subido correctamente: " + archivo.getName());
            } else {
                System.out.println("[FTP] Error al subir el archivo. " + ftp.getReplyString());
            }
        }
    }

    // Descarga un archivo del servidor al disco local
    private static void descargarArchivo(FTPSClient ftp, String nombreRemoto,
                                         String rutaLocal) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(rutaLocal + "/" + nombreRemoto)) {
            // retrieveFile descarga el archivo del directorio actual del servidor
            boolean ok = ftp.retrieveFile(nombreRemoto, fos);
            if (ok) {
                System.out.println("[FTP] Archivo descargado correctamente en: "
                        + rutaLocal + "/" + nombreRemoto);
            } else {
                System.out.println("[FTP] Error al descargar el archivo. " + ftp.getReplyString());
            }
        }
    }
}