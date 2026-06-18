package TunelLavado;

// Esta clase representa un contador compartido entre todos los hilos (coches)
// Varios hilos van a intentar incrementarlo "a la vez", por eso necesita ser seguro frente a concurrencia
public class ContadorLavados {

    // Variable que guarda el número total de coches lavados
    // No es estática porque vamos a usar UNA SOLA instancia compartida entre todos los hilos
    private int total = 0;

    // Método para incrementar el contador
    // Lo marcamos como synchronized para que solo un hilo a la vez pueda ejecutar este código
    // Esto evita el problema de "interferencia entre hilos" que se explica en los apuntes:
    // si dos hilos leen el valor "a la vez" y luego escriben, se podría perder un incremento
    public synchronized void incrementar() {
        total++;
    }

    // Método para consultar el valor actual
    // También lo hacemos synchronized para asegurar que se lee el valor más actualizado en memoria
    // (evita problemas de "inconsistencia de memoria" entre hilos)
    public synchronized int getTotal() {
        return total;
    }
}