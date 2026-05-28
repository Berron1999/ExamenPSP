package ClienteServidorContactos;

import java.io.Serializable;

// Serializable es obligatorio para poder enviar el objeto por el socket
public class Contacto implements Serializable {

    private String nombre;
    private String telefono;
    private String correo;

    public Contacto(String nombre, String telefono, String correo) {
        this.nombre   = nombre;
        this.telefono = telefono;
        this.correo   = correo;
    }

    public String getNombre()   { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo()   { return correo; }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | Teléfono: " + telefono + " | Correo: " + correo;
    }
}