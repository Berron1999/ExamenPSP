package org.iesch.psp.AlgoritmosDeEncriptacion.AES;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

// Cifra y descifra un fichero usando AES/CBC/PKCS5Padding
// Igual que MainCrypto de los apuntes
public class EjercicioAES {

    // Clave de cifrado (16 bytes = 128 bits)
    private static final byte[] key = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };

    // Vector de inicialización (IV)
    private static final byte[] iv = {
            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
            0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };

    private static final String path = "fichero_cifrado.dat";

    public static void main(String[] args) {
        cifrado();
        descifrado();
    }

    // Cifra texto y lo guarda en fichero
    private static void cifrado() {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);

            // Configuramos el cifrador AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(key, "AES"),
                    new IvParameterSpec(iv));

            // CipherOutputStream → todo lo que escribamos sale cifrado
            CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(cipherOut, "UTF-8"));

            writer.write("Mensaje secreto cifrado con AES");
            writer.newLine();
            writer.close();
            cipherOut.close();
            fileOut.close();

            System.out.println("[AES] Fichero cifrado correctamente: " + path);

        } catch (Exception e) {
            System.out.println("[AES] Error en el cifrado");
            e.printStackTrace();
        }
    }

    // Lee el fichero cifrado y lo descifra
    private static void descifrado() {
        try {
            FileInputStream fileIn = new FileInputStream(path);

            // Configuramos el descifrador con la misma clave y IV
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(key, "AES"),
                    new IvParameterSpec(iv));

            // CipherInputStream → todo lo que leamos sale descifrado
            CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(cipherIn, "UTF-8"));

            System.out.println("[AES] Texto descifrado:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
            reader.close();
            cipherIn.close();
            fileIn.close();

        } catch (Exception e) {
            System.out.println("[AES] Error en el descifrado");
            e.printStackTrace();
        }
    }
}