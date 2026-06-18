package TransferenciasBancariasSeguras;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ManejadorTransferencia extends Thread {

    private Socket socket;
    private Semaphore semaforo;

    public ManejadorTransferencia(Socket socket, Semaphore semaforo) {
        this.socket = socket;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        try {
            semaforo.acquire(); // solo 2 transferencias se procesan a la vez
        } catch (InterruptedException e) {
            e.printStackTrace();
            return; // no se ha adquirido el permiso, asi que no hay que liberarlo
        }

        // a partir de aqui ya tenemos el permiso, por eso el release() va en su propio finally
        try {
            // out antes que in en ambos lados, para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            DatosTransferencia datos = (DatosTransferencia) in.readObject();

            String textoOriginal = datos.getCuentaOrigen() + datos.getCuentaDestino() + datos.getImporte();
            String hmacCalculado = UtilHMAC.calcularHMAC(textoOriginal);

            String mensaje;
            if (hmacCalculado.equals(datos.getHmacEnviado())) {
                Thread.sleep(3000); // simula el tiempo de procesamiento
                mensaje = "Transferencia de " + datos.getImporte() + " de " + datos.getCuentaOrigen()
                        + " a " + datos.getCuentaDestino() + " procesada";
            } else {
                mensaje = "Transferencia rechazada: integridad comprometida";
            }

            String hmacRespuesta = UtilHMAC.calcularHMAC(mensaje);
            RespuestaTransferencia respuesta = new RespuestaTransferencia(mensaje, hmacRespuesta);
            out.writeObject(respuesta);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            semaforo.release(); // se libera siempre, haya pasado lo que haya pasado
        }
    }
}