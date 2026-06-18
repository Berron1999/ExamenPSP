package ServidorCalificacionesCifradas;

import java.io.Serializable;

public class DatosNota implements Serializable {

    private String alumno;
    private double nota;

    public DatosNota(String alumno, double nota) {
        this.alumno = alumno;
        this.nota = nota;
    }

    public String getAlumno() {
        return alumno;
    }

    public double getNota() {
        return nota;
    }
}