package org.iesch.psp.ej4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.crypto.SecretKey;

public class Descifrado {
    public static void main(String[] args) {
        try (BufferedReader consola = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Nombre: ");
            String nombre = consola.readLine();
            System.out.print("Texto cifrado: ");
            String texto = consola.readLine();

            if (nombre != null) {
                nombre = nombre.trim();
            }
            if (texto != null) {
                texto = texto.trim();
            }

            SecretKey clave = AESTools.generarClaveDesdeNombre(nombre);
            String descifrado = AESTools.descifrar(texto, clave);
            System.out.println("Descifrado: " + descifrado);
        } catch (IOException e) {
            System.out.println("Error en descifrado: " + e.getMessage());
        }
    }
}
