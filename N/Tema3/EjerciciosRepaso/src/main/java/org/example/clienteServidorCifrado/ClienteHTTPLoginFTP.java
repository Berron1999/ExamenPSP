package org.example.clienteServidorCifrado;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;

public class ClienteHTTPLoginFTP {

    private static final byte[] key = {
            0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,
            0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16
    };

    private static final byte[] iv = {
            0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,
            0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16
    };

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.print("Host FTP: ");
        String host = sc.nextLine();

        System.out.print("Usuario: ");
        String user = sc.nextLine();

        System.out.print("Contraseña: ");
        String pass = sc.nextLine();

        URL url = new URL("http://localhost:8080/login");
        HttpURLConnection con =
                (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();

        os.write((cifrar(host) + "\n").getBytes());
        os.write((cifrar(user) + "\n").getBytes());
        os.write((cifrar(pass) + "\n").getBytes());

        os.flush();

        Scanner response =
                new Scanner(con.getInputStream());

        System.out.println("Respuesta servidor: " + response.nextLine());

        os.close();
        response.close();
    }

    private static String cifrar(String texto) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(key,"AES"),
                new IvParameterSpec(iv));

        return Base64.getEncoder()
                .encodeToString(cipher.doFinal(texto.getBytes()));
    }
}

