package Servicios;

import DAO.LibroDaoImp;
import Interfaces.LibroDAO;

import java.util.InputMismatchException;
import java.util.Scanner;

/// En esta sección estarán las diferentes funcionalidades de Libros y Ejemplares.
public class LibrosServicios {
    private final Scanner sc = new Scanner(System.in);
    LibroDAO consulta = new LibroDaoImp();
    public void menuLibroEjemplar(){
        try{
            System.out.println("""
                \nBienvenido a la sección Libros y Ejemplares.
                ¿Qué acción deseas realizar?
                1.Acceder a Libros.
                2.Acceder a Ejemplares.
                3.Regresar al inicio.
                Ingresa tu opción:\s""");
            int opcion = sc.nextInt();
            switch(opcion){
                case 1 ->{}
                case 2 ->{}
                case 3 -> System.out.println("Regresando al menu principal.");
                default -> System.out.println("Ingrese una opción valida.");
            }
        }catch(InputMismatchException tipos){
            System.out.println("Ingrese los datos que se le solicitan.");
        }
    }

    //======================| MENU LIBROS. |===========================
    public void menuLibros(){
        int opcion = mensajeMenu("Libros");
        sc.nextLine();
        switch(opcion){
            //Opción mostrar Libros
            case 1 ->{}
            //Opción agregar Libros
            case 2 ->{}
            case 3 -> System.out.println("Regresando al menu inicial.");
            default -> System.out.println("Ingrese una opción correcta");
        }
    }

    //======================| MENU EJEMPLARES. |===========================
    public void menuEjemplares(){
        int opcion = mensajeMenu("Ejemplares");
        sc.nextLine();
        switch(opcion){
            //Opción mostrar Ejemplares
            case 1 ->{}
            //Opción agregar Ejemplares
            case 2 ->{}
            case 3 -> System.out.println("Regresando al menu inicial.");
            default -> System.out.println("Ingrese una opción correcta");
        }
    }

    //======================|  MOSTRAR LIBROS. |===========================
    public void obtenerLibro(){

    }

    //======================| AGREGAR LIBROS. |===========================
    public void agregarLibros(){
        System.out.print("""
                Agregar libros.
                Ingrese los datos que se le solicitan.
                """);
        System.out.print("ISBN del libro: ");
        String ISBN = sc.nextLine();
        //La primera validación empieza aquí.
        if(consulta.validarISBN(ISBN)){
            //Si regresa un true entonces existe el valor, por lo que no debemos guardarlo.
            return;
        }

        System.out.print("Titulo: ");
        String titulo = sc.nextLine();
        System.out.print("Categoria: ");
        String categoria = sc.nextLine();
        System.out.print("Autor: ");
        String autor = sc.nextLine();


    }


    //======================| MENSAJE GENÉRICO LIBROS Y EJEMPLARES. |===========================
    public int mensajeMenu(String area){
        int opcion = 0;
        try{
            System.out.println("\nBienvenido a la sección " + area);
            System.out.println("¿Que deseas realizar?");
            System.out.println("1.Buscar " + area);
            System.out.println("2.Agregar " + area);
            System.out.print("""
                    3.Regresar al inicio.
                    Ingresa tu opción:\s""");
            opcion = sc.nextInt();
        }catch(InputMismatchException tipo){
            System.out.println("Ingrese los datos que se le solicitan.");
        }
        return opcion;
    }
}
