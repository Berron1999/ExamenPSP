package org.iesch.psp.UDP.UDPMonitoreo;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class MonitorServer {
    public static void main(String[] args) {
        int puertoEscucha = 8000;
        boolean funcionando = true;

        try (DatagramSocket socket = new DatagramSocket(puertoEscucha)) {

            // EJERCICIO 4: Control de inactividad de 45 segundos
            socket.setSoTimeout(45000);
            System.out.println("MonitorServer iniciado en el puerto " + puertoEscucha);
            System.out.println("Esperando métricas o comandos...");

            while (funcionando) {
                try {
                    // 1. Preparar la caja grande para recibir cualquier cosa
                    // Hacemos el buffer grande porque un objeto ocupa más bytes que un texto
                    byte[] bufferRecepcion = new byte[4096];
                    DatagramPacket paqueteRecepcion = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);

                    // El servidor se queda bloqueado aquí esperando. Si pasan 45s, salta la excepción.
                    socket.receive(paqueteRecepcion);

                    // Extraemos la dirección de retorno (IP y Puerto) de la "caja" que acaba de llegar
                    InetAddress ipRemitente = paqueteRecepcion.getAddress();
                    int puertoRemitente = paqueteRecepcion.getPort();

                    // 2. ¿Es TEXTO (STATUS) o es un OBJETO (MetricaSistema)?
                    // Truco: Vamos a intentar convertirlo a String primero.
                    String posibleTexto = new String(paqueteRecepcion.getData(), 0, paqueteRecepcion.getLength()).trim();

                    if (posibleTexto.equals("STATUS")) {
                        // --- EJERCICIO 1: Respuesta al comando STATUS ---
                        System.out.println("\n[!] Recibido comando STATUS desde " + ipRemitente.getHostAddress());

                        String respuesta = "MONITOR ACTIVO";
                        byte[] bufferRespuesta = respuesta.getBytes();

                        DatagramPacket paqueteRespuesta = new DatagramPacket(
                                bufferRespuesta, bufferRespuesta.length, ipRemitente, puertoRemitente
                        );
                        socket.send(paqueteRespuesta);

                    } else {
                        // --- EJERCICIO 2: Procesar Objeto MetricaSistema ---
                        // Si no es el texto STATUS, asumimos que nos han enviado un objeto serializado
                        try {
                            ByteArrayInputStream bais = new ByteArrayInputStream(paqueteRecepcion.getData(), 0, paqueteRecepcion.getLength());
                            ObjectInputStream ois = new ObjectInputStream(bais);

                            // Deserializamos el objeto
                            MetricaSistema metrica = (MetricaSistema) ois.readObject();
                            ois.close();

                            // Evaluamos el uso de la CPU
                            if (metrica.getUsoCPU() <= 90) {
                                System.out.println("[INFO] Equipo " + metrica.getNombreEquipo() + " - CPU: " + metrica.getUsoCPU() + "%");
                            } else {
                                // Alerta si supera el 90%
                                System.out.println("[ALERTA] " + metrica.getNombreEquipo() + " al límite de CPU (" + metrica.getUsoCPU() + "%). Solicitando reducción...");

                                String mensajeAlerta = "ALERTA: REDUCIR CARGA";
                                byte[] bufferAlerta = mensajeAlerta.getBytes();

                                DatagramPacket paqueteAlerta = new DatagramPacket(
                                        bufferAlerta, bufferAlerta.length, ipRemitente, puertoRemitente
                                );
                                socket.send(paqueteAlerta);
                            }

                        } catch (Exception e) {
                            // Si falla al deserializar, era otra cosa que no entendemos
                            System.err.println("Datagrama no reconocido: No es STATUS ni un objeto MetricaSistema válido.");
                        }
                    }

                } catch (SocketTimeoutException e) {
                    // EJERCICIO 4: Captura del Timeout
                    System.out.println("\nTimeout: Apagando monitor por inactividad.");
                    funcionando = false; // Rompemos el bucle para cerrar el servidor
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}