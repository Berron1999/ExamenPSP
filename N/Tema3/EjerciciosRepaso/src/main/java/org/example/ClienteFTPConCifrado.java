package org.example;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

import org.apache.commons.net.ftp.FTPReply;


import javax.crypto.Cipher;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.cert.X509Certificate;
import java.util.Scanner;

/*Modifica el cliente FTP básico para que:

Antes de subir un archivo, lo cifre usando AES/CBC/PKCS5Padding.

Suba únicamente el fichero cifrado.

Cuando se descargue un archivo, lo descifre automáticamente.

No se envíe información en claro al servidor.*/
public class ClienteFTPConCifrado {

    // 🔐 Clave AES 128 bits
    private static final byte[] key = {
            0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,
            0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16
    };

    private static final byte[] iv = {
            0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,
            0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16
    };

    public static void main(String[] args) {

        String server = "eu-central-1.sftpcloud.io";
        String user = "df94966ffff2435db5667ed37341912c";
        String pass = "LQzhxAfi93iqyTNzpnPGK0UFArqjexha";

        FTPSClient ftp = new FTPSClient("TLS", false);
        Scanner sc = new Scanner(System.in);

        try {

            ftp.setTrustManager(new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String s) {}
                public void checkServerTrusted(X509Certificate[] xcs, String s) {}
                public X509Certificate[] getAcceptedIssuers() { return null; }
            });

            ftp.connect(server,21);

            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.disconnect();
                System.out.println("Conexión rechazada.");
                return;
            }

            if (!ftp.login(user, pass)) {
                System.out.println("Credenciales incorrectas.");
                return;
            }

            ftp.execPBSZ(0);
            ftp.execPROT("C");
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPSClient.BINARY_FILE_TYPE);

            boolean salir = false;
            while (!salir) {

                System.out.println("\n--- MENÚ FTP SEGURO ---");
                System.out.println("1. Listar archivos");
                System.out.println("2. Subir archivo CIFRADO");
                System.out.println("3. Descargar y DESCIFRAR");
                System.out.println("4. Salir");
                System.out.print("Opción: ");

                String opcion = sc.nextLine();

                switch (opcion) {

                    case "1":
                        listDirectory(ftp);
                        break;

                    case "2":
                        System.out.print("Archivo LOCAL a subir: ");
                        String local = sc.nextLine();

                        File original = new File(local);
                        if (!original.exists()) {
                            System.out.println("Archivo no existe.");
                            break;
                        }

                        File cifrado = new File("temp_cifrado.dat");
                        cifrarArchivo(original, cifrado);

                        try (FileInputStream fis = new FileInputStream(cifrado)) {
                            ftp.storeFile(cifrado.getName(), fis);
                        }

                        System.out.println("Archivo cifrado subido.");
                        cifrado.delete();
                        break;

                    case "3":
                        System.out.print("Archivo REMOTO a descargar: ");
                        String remoto = sc.nextLine();

                        File descargado = new File("temp_descargado.dat");

                        try (FileOutputStream fos = new FileOutputStream(descargado)) {
                            ftp.retrieveFile(remoto, fos);
                        }

                        descifrarArchivo(descargado, new File("descifrado.txt"));
                        System.out.println("Archivo descargado y descifrado.");
                        descargado.delete();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cifrarArchivo(File entrada, File salida) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec sk = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, sk, ivSpec);

        try (FileInputStream fis = new FileInputStream(entrada);
             FileOutputStream fos = new FileOutputStream(salida);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
    }

    // 🔓 DESCIFRAR ARCHIVO
    private static void descifrarArchivo(File entrada, File salida) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec sk = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, sk, ivSpec);

        try (FileInputStream fis = new FileInputStream(entrada);
             CipherInputStream cis = new CipherInputStream(fis, cipher);
             FileOutputStream fos = new FileOutputStream(salida)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    private static void listDirectory(FTPSClient ftp) throws IOException {
        FTPFile[] files = ftp.listFiles();
        for (FTPFile file : files) {
            String tipo = file.isDirectory() ? "<DIR>" : "<FIL>";
            System.out.println(tipo + " " + file.getName());
        }
    }
}