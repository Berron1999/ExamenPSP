import java.util.Scanner;

public class Ejercicio1ParesImpares {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Dime un numero: ");
        int numero = scan.nextInt();

        if(!esPar(numero)){
            System.out.println("El numero: "+numero+ " es impar");
        }else System.out.println("El numero: "+ numero+" es par");



    }
    static boolean esPar(int numero){

        if(numero%2 !=0){
            return false;
        }else return true;

    }

}
