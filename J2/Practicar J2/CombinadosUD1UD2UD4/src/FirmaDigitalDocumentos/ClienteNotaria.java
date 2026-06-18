package FirmaDigitalDocumentos;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.util.Scanner;

public class ClienteNotaria {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // cada cliente genera su propia identidad digital (par de claves RSA)
            KeyPair claves = UtilFirma.generarClaves();

            System.out.print("Contenido del documento: ");
            String contenido = sc.nextLine();

            byte[] firma = UtilFirma.firmar(contenido, claves.getPrivate());

            // OJO: aqui se podria modificar "contenido" despues de firmar para probar
            // que el servidor detecta la manipulacion y rechaza el documento

            DatosDocumento datos = new DatosDocumento(contenido, firma, claves.getPublic().getEncoded());

            Socket socket = new Socket("localhost", 7600);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(datos);

            RespuestaVerificacion respuesta = (RespuestaVerificacion) in.readObject();
            System.out.println(respuesta.getMensaje());

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}