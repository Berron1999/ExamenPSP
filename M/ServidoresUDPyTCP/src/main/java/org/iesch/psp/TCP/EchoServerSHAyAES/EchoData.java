package org.iesch.psp.TCP.EchoServerSHAyAES;
//EchoData: Almacén compartido (y sincronizado) que guarda las estadísticas de uso.
// Es el recurso crítico donde todos los hilos intentan escribir a la vez.
public class EchoData {
    private int totalMensajes = 0;
    private int totalCaracteres = 0;

    // EJERCICIO 4: Añadimos synchronized para evitar que los hilos se pisen al sumar
    public synchronized void registrarMensaje(String mensaje) {
        totalMensajes++;
        totalCaracteres += mensaje.length();
    }

    // También sincronizamos la lectura para evitar leer mientras otro escribe
    public synchronized String getEstadisticas() {
        return totalMensajes + " mensajes procesados y " + totalCaracteres + " caracteres.";
    }
}