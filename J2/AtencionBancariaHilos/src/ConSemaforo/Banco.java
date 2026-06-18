package ConSemaforo;

public class Banco {

    public static void main(String[] args) throws InterruptedException {

        int numVentanillas = 3; // Configurable
        int numClientes = 8;    // Configurable

        // Creamos las ventanillas
        Ventanilla[] ventanillas = new Ventanilla[numVentanillas];
        for (int i = 0; i < numVentanillas; i++) {
            ventanillas[i] = new Ventanilla(i + 1);
        }

        // Creamos y lanzamos los hilos cliente
        Cliente[] clientes = new Cliente[numClientes];
        for (int i = 0; i < numClientes; i++) {
            clientes[i] = new Cliente(i + 1, ventanillas);
            clientes[i].start();
        }

        // Esperamos a que TODOS los clientes terminen
        for (int i = 0; i < numClientes; i++) {
            clientes[i].join();
        }

        // Resultado final: resumen por ventanilla y total del banco
        System.out.println("\n========== RESULTADO FINAL ==========");
        double totalBanco = 0;
        for (Ventanilla v : ventanillas) {
            System.out.println("Ventanilla " + v.getId()
                    + " → Clientes atendidos: " + v.getClientesAtendidos()
                    + " | Dinero gestionado: " + v.getDineroGestionado() + "€");
            totalBanco += v.getDineroGestionado();
        }
        System.out.println("Total gestionado por el banco: " + totalBanco + "€");
    }
}