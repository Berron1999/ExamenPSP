package org.example.service;

import com.google.gson.Gson;
import org.example.model.DatosDiarios;
import org.example.model.ResultadoFicheros;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LectorFichero {

    private final Gson gson = new Gson();

    public ResultadoFicheros procesar(Path ruta){

        try {
            //Leer el fichero entero como texto.
            String json = Files.readString(ruta);

            //Pasamos la informacion a los atributos de la clase DatosDiarios
            DatosDiarios datosDiarios = gson.fromJson(json, DatosDiarios.class);

            //Creamos el resultado
            ResultadoFicheros res = new ResultadoFicheros();

            //Pasar la temperatura max y minima a float

            Float tmax = datosDiarios.getTmaxFloat();
            Float tmin = datosDiarios.getTminFloat();

            if (tmax!=null){
                res.setMax(tmax, datosDiarios.getFecha(), datosDiarios.getHoratmax(), datosDiarios.getNombre());
            }

            if (tmin!=null){
                res.setMin(tmin, datosDiarios.getFecha(), datosDiarios.getHoratmin(), datosDiarios.getNombre());
            }
            return res;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
