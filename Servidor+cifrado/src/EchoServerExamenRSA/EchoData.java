package EchoServerExamenRSA;

/*
 * Clase compartida entre hilos del servidor para guardar:
 *   - número de mensajes procesados
 *   - número total de caracteres
 *
 * Todos los métodos son SYNCHRONIZED para evitar condiciones de carrera
 * cuando varios hilos actualizan estos contadores a la vez.
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
