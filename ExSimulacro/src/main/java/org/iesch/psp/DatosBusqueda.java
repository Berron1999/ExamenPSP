package org.iesch.psp;

import java.io.Serializable;

public class DatosBusqueda implements Serializable {
    private static final long serialVersionUID = 1L;

    // URL de la pagina a descargar.
    private final String url;
    // Palabra o cadena a buscar en el contenido.
    private final String cadena;

    public DatosBusqueda(String url, String cadena) {
        this.url = url;
        this.cadena = cadena;
    }

    public String getUrl() {
        return url;
    }

    public String getCadena() {
        return cadena;
    }
}
