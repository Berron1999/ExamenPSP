package EchoServerExamenAES;

/*
 * Clase compartida entre hilos del servidor para guardar:
 *   - número de mensajes procesados
 *   - número total de caracteres
 *
 * Todos los métodos son SYNCHRONIZED para evitar condiciones de carrera
 * cuando varios hilos (uno por cliente) actualizan estos contadores a la vez.
 * Sin synchronized, con 100 clientes x 100 mensajes = 10000, el contador
 * final podría salir menor (ej. 9876) por accesos solapados.
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
