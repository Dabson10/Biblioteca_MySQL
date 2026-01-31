package org.example;

import Conexion.Conexion;

import java.sql.Connection;
import java.util.Scanner;

public class Main {

    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean acceso = true;
        Main principal = new Main();
        Conexion conect = new Conexion();
        Connection validar = conect.conectarse();
        //Este if sirve para validar la conexion a base de datos.
        //Si regresa un null entonces no mostramos el menu principal
        if(validar != null){
            while(acceso){
                acceso = principal.menuPrincipal();
            }
        }else{
            System.out.println("No se pudo realiazar la conexion a base de datos.");
        }

    }



    public boolean menuPrincipal(){
        boolean salida = true;
        int opcion = 0;
        System.out.print("""
                Bienvenido a la biblioteca.
                ¿Que acción deseas realizar?
                1.Personas.
                2.Libros y Ejemplares.
                3.Prestamos.
                4.Salir
                Ingrese la opción:\s""");
        opcion = sc.nextInt();

        switch(opcion){
            case 1 ->{}
            case 2 ->{}
            case 3->{}
            case 4 ->{
                System.out.println("Hasta luego.");
                sc.close();
                salida = false;
            }
            default ->System.out.println("Ingrese una opción valida");
        }

        return salida;
    }

}