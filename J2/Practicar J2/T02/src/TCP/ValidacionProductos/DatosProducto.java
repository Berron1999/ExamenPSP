package TCP.ValidacionProductos;

import java.io.Serializable;

public class DatosProducto implements Serializable {

    private String codigo;
    private double precio;

    public DatosProducto(String codigo, double precio) {
        this.codigo = codigo;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return "DatosProducto{codigo='" + codigo + "', precio=" + precio + "}";
    }
}