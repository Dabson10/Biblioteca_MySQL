package org.example;

import DAO.Conexion;
import DAO.PersonaDaoImpl;
import Entidades.Usuarios.Bibliotecario;
import Entidades.Usuarios.Persona;
import Servicios.LibrosServicios;
import Servicios.UsuariosServicio;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    Scanner sc = new Scanner(System.in);
    UsuariosServicio servicioUsu = new UsuariosServicio();
    LibrosServicios serviciosLibros = new LibrosServicios();

    public static void main(String[] args) {
        boolean acceso = true;

        Main principal = new Main();
            while(acceso){
                acceso = principal.menuPrincipal();
            }

    }



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