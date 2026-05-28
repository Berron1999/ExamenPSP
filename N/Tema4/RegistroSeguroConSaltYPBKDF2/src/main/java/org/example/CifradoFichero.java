package org.example;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Scanner;

/*Cifre un texto introducido por el usuario usando AES/CBC/PKCS5Padding.

Guarde el resultado en un fichero.

Posteriormente descifre el fichero y muestre el contenido original.*/
public class CifradoFichero {

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

        System.out.print("Texto a cifrar: ");
        String texto = sc.nextLine();

        cifrar(texto);
        descifrar();
    }

    private static void cifrar(String texto) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec sk = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, sk, ivSpec);

        FileOutputStream fos = new FileOutputStream("cifrado.txt");
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        cos.write(texto.getBytes());
        cos.close();

        System.out.println("Texto cifrado en fichero.");
    }

    private static void descifrar() throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec sk = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, sk, ivSpec);

        FileInputStream fis = new FileInputStream("cifrado.txt");
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        BufferedReader br = new BufferedReader(new InputStreamReader(cis));

        System.out.println("Texto descifrado:");
        System.out.println(br.readLine());

        br.close();
    }
}

