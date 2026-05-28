package org.example.clienteServidorCifrado;


/*🔹 Servidor HTTP

Escuche en el puerto 8080.

Implemente una ruta /login.

El cliente enviará:

host FTP

usuario FTP

contraseña FTP
(todos cifrados con AES y en Base64).

El servidor:

Descifra los datos.

Intenta conectarse al servidor FTP con esas credenciales.

Devuelve:

LOGIN OK

LOGIN ERROR*/
import com.sun.net.httpserver.*;

import org.apache.commons.net.ftp.FTPSClient;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.Base64;
import java.util.concurrent.Executors;

public class ServidorHTTPLoginFTP {

    private static final byte[] key = {
            0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,
            0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16
    };

    private static final byte[] iv = {
            0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,
            0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16
    };

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.setExecutor(Executors.newFixedThreadPool(5));

        server.createContext("/login", new LoginHandler());

        server.start();
        System.out.println("Servidor HTTP iniciado en puerto 8080");
    }

    static class LoginHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {

            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody()))) {

                String hostEnc = br.readLine();
                String userEnc = br.readLine();
                String passEnc = br.readLine();

                String host = descifrar(hostEnc);
                String user = descifrar(userEnc);
                String pass = descifrar(passEnc);

                FTPSClient ftp = new FTPSClient("TLS", false);

                ftp.connect(host,21);
                boolean loginOk = ftp.login(user, pass);
                ftp.disconnect();

                String respuesta = loginOk ? "LOGIN OK" : "LOGIN ERROR";

                byte[] bytes = respuesta.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();

            } catch (Exception e) {
                exchange.sendResponseHeaders(500, -1);
            }
        }
    }

    private static String descifrar(String textoBase64) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(key,"AES"),
                new IvParameterSpec(iv));

        byte[] descifrado =
                cipher.doFinal(Base64.getDecoder().decode(textoBase64));

        return new String(descifrado);
    }
}

