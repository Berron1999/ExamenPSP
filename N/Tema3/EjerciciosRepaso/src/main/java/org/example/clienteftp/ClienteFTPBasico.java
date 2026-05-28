package org.example.clienteftp;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.cert.X509Certificate;
import java.util.Scanner;
/**/
public class ClienteFTPBasico {
    public static void main(String[] args) {

        String server = "eu-central-1.sftpcloud.io";
        String user = "263077a4c34e4fe4af1d36f8d0fe08b9";
        String pass = "b4eGj0Sj3Uqy0OZvuTEYHwMascrrbTfH";


        FTPSClient ftp = new FTPSClient("TLS", false);
        Scanner sc = new Scanner(System.in);

        try {
            ftp.setTrustManager(new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String s) {}
                public void checkServerTrusted(X509Certificate[] xcs, String s) {}
                public X509Certificate[] getAcceptedIssuers() { return null; }
            });

            System.out.println("Conectando a " + server + "...");
            ftp.connect(server,21);


            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.disconnect();
                System.out.println("Error: Conexión rechazada.");
                return;
            }

            if (!ftp.login(user, pass)) {
                System.out.println("Error: Credenciales incorrectas.");
                return;
            }

            ftp.execPBSZ(0);
            ftp.execPROT("C");
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPSClient.BINARY_FILE_TYPE);

            boolean salir = false;
            while (!salir) {
                System.out.println("\n--- MENÚ FTP ---");
                System.out.println("1. Listar archivos remotos");
                System.out.println("2. Subir archivo");
                System.out.println("3. Descargar archivo");
                System.out.println("4. Salir");
                System.out.print("Elige una opción: ");

                String opcion = sc.nextLine();

                switch (opcion) {
                    case "1":
                        listDirectory(ftp);
                        break;
                    case "2":
                        System.out.print("Nombre del archivo LOCAL a subir: ");
                        String localName = sc.nextLine();
                        File fSubir = new File(localName);
                        if (fSubir.exists()) {
                            try (FileInputStream fis = new FileInputStream(fSubir)) {
                                if (ftp.storeFile(fSubir.getName(), fis)) {
                                    System.out.println("Estado: " + ftp.getReplyString());
                                }
                            }
                        } else {
                            System.out.println("Error: El archivo local no existe.");
                        }
                        break;
                    case "3":
                        System.out.print("Nombre del archivo REMOTO a descargar: ");
                        String remoteName = sc.nextLine();

                        String destinoLocal="C:/Users/dam2/Downloads/" +remoteName;
                        try (FileOutputStream fos = new FileOutputStream(destinoLocal)) {
                            if (ftp.retrieveFile(remoteName, fos)) {
                                System.out.println("Estado: " + ftp.getReplyString());
                            } else {
                                System.out.println("No se pudo descargar.");
                            }
                        }
                        break;
                    case "4":
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }

            ftp.logout();
            ftp.disconnect();
            System.out.println("Conexión cerrada.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void listDirectory(FTPSClient ftp) throws IOException {
        System.out.println("\n[LISTADO REMOTO]");
        FTPFile[] files = ftp.listFiles();
        for (FTPFile file : files) {
            String tipo = file.isDirectory() ? "<DIR>" : "<FIL>";
            System.out.println(tipo + " " + file.getName());
        }
        System.out.println("Respuesta servidor: " + ftp.getReplyString());
    }
}


