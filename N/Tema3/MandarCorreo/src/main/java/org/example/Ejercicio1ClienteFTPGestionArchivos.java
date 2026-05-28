package org.example;

import org.apache.commons.net.ftp.FTPSClient;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Ejercicio1ClienteFTPGestionArchivos {
    public static void main(String[] args) {

        /*Desarrolla una aplicación cliente que se conecte a un servidor FTP utilizando los
        protocolos estándar. El programa deberá permitir al usuario realizar operaciones
        básicas de gestión de archivos remotos.
        Requisitos:
        • Conectarse a un servidor FTP indicando:
        • Dirección del servidor
        • Usuario
        • Contraseña
        • Mostrar el contenido del directorio actual del servidor.
        • Permitir:
        • Subir un archivo al servidor.
        • Descargar un archivo del servidor.
        • Gestionar correctamente errores de conexión y autenticación.

        • Mostrar por consola los mensajes de estado de la comunicación cliente-
        servidor.*/

        FTPSClient ftp = new FTPSClient("TLS", false);

        try{
            ftp.setTrustManager(new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
