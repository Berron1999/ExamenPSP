package VotacionElectronicaSincronizada;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;

public class ManejadorVoto extends Thread {

    private Socket socket;
    private CyclicBarrier barrera;
    private ContadorVotos contador;

    public ManejadorVoto(Socket socket, CyclicBarrier barrera, ContadorVotos contador) {
        this.socket = socket;
        this.barrera = barrera;
        this.contador = contador;
    }

    @Override
    public void run() {
        try {
            // out antes que in en ambos lados, para evitar deadlock
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            DatosVoto datos = (DatosVoto) in.readObject();
            String opcion = UtilVoto.descifrar(datos.getOpcionCifrada(), datos.getIv());

            contador.incrementar(opcion);

            // se bloquea aqui hasta que los otros 2 votantes tambien lleguen
            barrera.await();

            // al llegar aqui, los 3 votos ya estan contados, asi que el resumen es definitivo
            RespuestaVotacion respuesta = new RespuestaVotacion(contador.obtenerResumen());

            try {
                out.writeObject(respuesta);
            } catch (java.io.IOException e) {
                // el cliente se desconecto antes de poder recibir la respuesta
                // no afecta a los otros votantes, asi que solo lo avisamos por consola
                System.out.println("Aviso: un cliente se desconecto antes de recibir el resultado");
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}