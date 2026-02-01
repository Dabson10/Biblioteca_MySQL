package org.example;

import DAO.Conexion;
import DAO.PersonaDaoImpl;
import Entidades.Usuarios.Bibliotecario;
import Entidades.Usuarios.Persona;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        boolean acceso = true;

//        Persona ps= new Bibliotecario("USUA_0002","Kevin", "Almaraz", "tadeo@gmail.com", "perro");
//        PersonaDaoImpl insert = new PersonaDaoImpl();
//        insert.agregarPersona(ps, "perro", "Bibliotecario");
        Main principal = new Main();
            while(acceso){
                acceso = principal.menuPrincipal();
            }

    }



    public boolean menuPrincipal(){
        boolean salida = true;
        int opcion = 0;
        try{

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
                case 3 ->{}
                case 4 ->{
                    System.out.println("Hasta luego.");
                    sc.close();
                    salida = false;
                }
                default ->System.out.println("Ingrese una opción valida");
            }
        }catch(InputMismatchException tipo){
            System.out.println("Ingrese datos correctos por favor: " + tipo.getCause());
            sc.nextLine();
        }

        return salida;
    }

}