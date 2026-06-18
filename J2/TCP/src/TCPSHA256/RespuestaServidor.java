package TCPSHA256;

import java.io.Serializable;
import java.time.LocalDateTime;

// Objeto que el servidor devuelve al cliente
public class RespuestaServidor implements Serializable {

    private String nombreUsuario;
    private String hashGenerado;           // SHA-256 de nombreUsuario + mensaje
    private int numeroCaracteresMensaje;   // Longitud del mensaje
    private LocalDateTime fechaRecepcion;  // Momento en que el servidor recibió el objeto

    public RespuestaServidor(String nombreUsuario, String hashGenerado,
                             int numeroCaracteresMensaje, LocalDateTime fechaRecepcion) {
        this.nombreUsuario = nombreUsuario;
        this.hashGenerado = hashGenerado;
        this.numeroCaracteresMensaje = numeroCaracteresMensaje;
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public String getHashGenerado() { return hashGenerado; }
    public int getNumeroCaracteresMensaje() { return numeroCaracteresMensaje; }
    public LocalDateTime getFechaRecepcion() { return fechaRecepcion; }
}