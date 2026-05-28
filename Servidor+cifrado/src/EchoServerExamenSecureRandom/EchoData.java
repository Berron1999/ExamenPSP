package EchoServerExamenSecureRandom;

/*
 * Clase compartida entre hilos del servidor: contadores de mensajes y
 * caracteres procesados. Métodos SYNCHRONIZED para evitar condiciones
 * de carrera con 100 clientes concurrentes.
 */
public class EchoData {

    private int mensajes   = 0;
    private int caracteres = 0;

    public synchronized void addMensaje(String mensaje) {
        mensajes++;
        caracteres += mensaje.length();
    }

    public synchronized int getMensajes()   { return mensajes; }
    public synchronized int getCaracteres() { return caracteres; }

    @Override
    public synchronized String toString() {
        return "Mensajes procesados: " + mensajes + " | Caracteres procesados: " + caracteres;
    }
}
