package Servicios;

import DAO.EjemplarDaoImpl;
import DAO.LibroDaoImp;
import Interfaces.EjemplarDao;
import Interfaces.LibroDAO;
import Utilidades.GenerarID;

import java.util.InputMismatchException;
import java.util.Scanner;

/// En esta sección estarán las diferentes funcionalidades de Libros y Ejemplares.
public class LibrosServicios {
    private final Scanner sc = new Scanner(System.in);
    LibroDAO consultaLibros = new LibroDaoImp();
    EjemplarDao consultaEjemplar = new EjemplarDaoImpl();
    GenerarID generarID = new GenerarID();
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
                case 1 -> menuLibros();
                case 2 -> menuEjemplares();
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
            case 2 -> agregarLibros();
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
    public boolean agregarLibros(){
        boolean acceso = false;
        System.out.print("""
                \nAgregar libros.
                Ingrese los datos que se le solicitan.
                El año del libro deberá ser ingresada por completo (2020)
                """);
        System.out.print("ISBN del libro: ");
        String ISBN = sc.nextLine();
        //La primera validación empieza aquí.
        if(consultaLibros.validarISBN(ISBN)){
            //Si regresa un true entonces existe el valor, por lo que no debemos guardarlo.
            return false;
        }

        System.out.print("Titulo: ");
        String titulo = sc.nextLine().trim();
        System.out.print("Categoria: ");
        String categoria = sc.nextLine().trim();
        System.out.print("Año de salida: ");
        String year = sc.nextLine().trim();
        System.out.println(year.length());
        if(year.length() < 4 ){
            System.out.println("Ingrese una año como: (2020) con 4 dígitos.");
            return false;
        }
        System.out.print("Autor: ");
        String autor = sc.nextLine().trim();

        //Generamos él prefijó, el cual en este caso será realmente grande.
        String prefijo = (categoria.substring(0,2) + autor.substring(0,2)+ titulo.substring(0, 2) + year.substring(2, 4)).toUpperCase();
        //Ahora guardamos los valores en la base de datos, sabiendo que no hay ISBN repetidos.
        boolean agregado = consultaLibros.setLibro(ISBN, titulo, categoria, autor, prefijo);
        if(agregado){
            //Si se agrego el libro entonces procedemos con la función de cantidad de ejemplares.
            agregarEjemplares(prefijo, ISBN);
        }
        return acceso;
    }

    //======================| AGREGAR EJEMPLARES. |===========================
    public void agregarEjemplares(String prefijo, String ISBN){
        boolean agregado = false;
        int contador = 0;
        try{
            System.out.print("Ingresa la ubicación del libro: ");
            String ubicacion = sc.nextLine();
            System.out.print("Tipo de libro(Virtual o físico): ");
            String tipo = sc.nextLine();
            System.out.print("Cantidad de ejemplares a guardar: ");
            int cantidad = sc.nextInt();
            for(int i = 0; i < cantidad; i++){
                /// Ahora generamos el ID del libro, que este irá en aumento.
                //En esta parte el numero empezara por i(0) y se aumentara en 1 siempre
                String ID = generarID.generarID(prefijo , (1 + i));
                //Aquí realizamos la consulta
                agregado = consultaEjemplar.setEjemplar(ID, ISBN, ubicacion, tipo);
                if(agregado){
                    //Si se agregó entonces agregamos en un contador y guardamos la
                    // cantidad de ejemplares que se guardaron.
                    contador++;
                }
            }
            System.out.println("Se guardaron " + contador + " de " + cantidad + " ejemplares.");
        }catch (InputMismatchException tipo){
            System.out.println("Ingrese correctamente los datos que le piden.");
        }
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

    //======================| BUCLES PARA AGREGAR LIBROS. |===========================
    public void bucleLibros(){
        boolean continuar = false;
        for(int i = 0; i < 3; i++){
            continuar = agregarLibros();
            if(continuar){
                //Si continuar es true significa que se guardó el libro correctamente, por lo que debemos terminar el bulce.
                break;
            }else{
                System.out.println("Estas en el intento " + ( i + 1) + ", el limite es: 3");
            }
        }
    }

    public void bucleEjemplares(){

    }
}
