package org.example;


import Servicios.LibrosServicios;
import Servicios.PrestamosServicios;
import Servicios.UsuariosServicio;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    Scanner sc = new Scanner(System.in);
    UsuariosServicio servicioUsu = new UsuariosServicio();
    LibrosServicios serviciosLibros = new LibrosServicios();
    PrestamosServicios servicioPrest = new PrestamosServicios();

    /**
     *  Este es el método principal, en donde se realizara el bucle para
     *  que el programa funcione correctamente.
     */
    public static void main(String[] args) {
        boolean acceso = true;

        Main principal = new Main();

        while (acceso) {
            acceso = principal.menuPrincipal();
        }

    }


    /**
     * Esta función sirve como menu para seleccionar a que aréa se dirigirá el usuario. <br>
     * Maneja errores básicos como {@code InputMismatchException}
     * @return Si ingresa del 1 al 3 regresara un true, si regresa un 4 un false para asi terminar el programa.
     */
    public boolean menuPrincipal(){
        boolean salida = true;
        int opcion;
        try{

            System.out.print("""
                \nBienvenido a la biblioteca.
                ¿Que acción deseas realizar?
                1.Personas.
                2.Libros y Ejemplares.
                3.Prestamos.
                4.Salir
                Ingrese la opción:\s""");
            opcion = sc.nextInt();

            switch(opcion){
                case 1 -> servicioUsu.menuPersonas();
                case 2 -> serviciosLibros.menuLibroEjemplar();
                case 3 -> servicioPrest.menuPrestamo();
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