package TCPRegistroTemperaturas;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HiloCliente extends Thread{
    private Socket socket;
    public HiloCliente(Socket socket) {
        this.socket = socket;
    }

    public void run(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
