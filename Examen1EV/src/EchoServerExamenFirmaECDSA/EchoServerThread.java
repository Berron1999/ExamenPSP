package EchoServerExamenFirmaECDSA;

import java.io.*;
import java.net.Socket;

// Hilo del servidor: recibe "mensaje|firma_ecdsa", verifica con la pública EC,
// hace eco y devuelve "mensaje|firma_ecdsa".
public class EchoServerThread extends Thread {

    private Socket   socket;
    private EchoData echoData;

    public EchoServerThread(Socket socket, EchoData echoData) {
        this.socket   = socket;
        this.echoData = echoData;
    }

    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter    salida  = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String linea;
            while ((linea = entrada.readLine()) != null) {

                int sep = linea.lastIndexOf(FirmaECDSA.SEP);
                if (sep < 0) {
                    System.out.println("[Servidor] Línea sin separador, ignorada.");
                    continue;
                }
                String mensaje       = linea.substring(0, sep);
                String firmaRecibida = linea.substring(sep + 1);

                if (!FirmaECDSA.verificar(mensaje, firmaRecibida)) {
                    System.out.println("[Servidor] FIRMA ECDSA INVÁLIDA para: " + mensaje);
                    continue;
                }

                if (mensaje.equals(".")) {
                    System.out.println("[Servidor] Orden de parada recibida.");
                    salida.println(FirmaECDSA.empaquetar(echoData.toString()));
                    System.exit(0);
                }

                echoData.addMensaje(mensaje);
                salida.println(FirmaECDSA.empaquetar(mensaje));
                System.out.println("[Servidor] Eco firmado para: " + mensaje);
            }

        } catch (Exception e) {
            System.out.println("[Servidor] Error con cliente: " + e.getMessage());
        }
    }
}
