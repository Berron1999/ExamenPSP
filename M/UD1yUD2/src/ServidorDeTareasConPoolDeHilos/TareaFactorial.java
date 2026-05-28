package ServidorDeTareasConPoolDeHilos;

import java.math.BigInteger;
import java.util.concurrent.Callable;

// Callable<BigInteger> → la tarea devuelve un BigInteger como resultado
public class TareaFactorial implements Callable<BigInteger> {

    private int numero;

    public TareaFactorial(int numero) {
        this.numero = numero;
    }

    // call() es el equivalente a run() pero puede devolver un valor
    @Override
    public BigInteger call() throws Exception {
        System.out.println("[Pool] Calculando factorial de " + numero
                + " en hilo: " + Thread.currentThread().getName());

        // Simulamos que es un cálculo pesado
        Thread.sleep(2000);

        BigInteger resultado = BigInteger.ONE;
        for (int i = 2; i <= numero; i++) {
            resultado = resultado.multiply(BigInteger.valueOf(i));
        }
        return resultado;
    }
}