package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class EjercicioMayor3 {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Dime un numero: ");
        int num1 = scan.nextInt();
        System.out.println("Dime un numero: ");
        int num2 = scan.nextInt();
        System.out.println("Dime un numero: ");
        int num3 = scan.nextInt();

        if(num1>num2 && num2>num3){
            System.out.println("El orden de los numeros es:"+num1+" "+num2+" "+num3);
        }else if(num1>num3 && num3>num2){
            System.out.println("El orden de los numeros es:"+num1+" "+num3+" "+num2);
        }else if(num2>num3 && num3>num1){
            System.out.println("El orden de los numeros es:"+num2+" "+num3+" "+num1);
        }else if(num2>num1 && num1>num3){
            System.out.println("El orden de los numeros es:"+num2+" "+num1+" "+num3);
        }else if(num3>num2 && num2>num1){
            System.out.println("El orden de los numeros es:"+num3+" "+num2+" "+num1);
        }else if(num3>num1 && num1>num2){
            System.out.println("El orden de los numeros es:"+num3+" "+num1+" "+num2);
        }

        //Otra opcion seria con listas que es mucho mas fácil

        System.out.print("Dime numeros hasta que insertes un -1 ");
        int numeroLista=0;
        ArrayList<Integer> listaNumeros= new ArrayList<>();
        while(numeroLista!=-1){
            System.out.print("Dime el primer número: ");
            numeroLista= scan.nextInt();
            listaNumeros.add(numeroLista);
        }

        /*Ahora vamos a sacar la lista por orden en la que lo ha metido el usuario
        for(int n: listaNumeros){
            System.out.println(n);
        } Ahora ordenada*/

        System.out.println(listaNumeros);
        listaNumeros.sort(null);
        System.out.println(listaNumeros);

    }

}

