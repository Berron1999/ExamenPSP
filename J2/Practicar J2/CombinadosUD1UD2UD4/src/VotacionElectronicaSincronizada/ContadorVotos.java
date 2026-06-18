package VotacionElectronicaSincronizada;

public class ContadorVotos {

    private int votosA = 0;
    private int votosB = 0;
    private int votosC = 0;

    public synchronized void incrementar(String opcion) {
        if (opcion.equals("A")) {
            votosA++;
        } else if (opcion.equals("B")) {
            votosB++;
        } else if (opcion.equals("C")) {
            votosC++;
        }
    }

    public synchronized String obtenerResumen() {
        return "A: " + votosA + ", B: " + votosB + ", C: " + votosC;
    }
}