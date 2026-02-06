package Servicios;

import DAO.EjemplarDaoImpl;
import DAO.LibroDaoImp;
import Entidades.Ejemplar;
import Entidades.EjemplarDAO;
import Entidades.Libro;
import Interfaces.EjemplarDao;
import Interfaces.LibroDAO;
import Utilidades.GenerarID;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/// En esta sección estarán las diferentes funcionalidades de Libros y Ejemplares.
public class LibrosServicios {
    private final Scanner sc = new Scanner(System.in);
    LibroDAO consultaLibros = new LibroDaoImp();
    EjemplarDao consultaEjemplar = new EjemplarDaoImpl();
    GenerarID generarID = new GenerarID();
    public void menuLibroEjemplar(){
        try{
            System.out.print("""
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
            case 1 -> obtenerLibro();
            //Opción agregar Libros
            case 2 -> bucleLibros();
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
            case 1 -> mostrarEjemplares();
            //Opción agregar Ejemplares
            case 2 ->agregarMasEjemplares();
            case 3 -> System.out.println("Regresando al menu inicial.");
            default -> System.out.println("Ingrese una opción correcta");
        }
    }

    //======================|  MOSTRAR LIBROS. |===========================
    public void obtenerLibro(){
        try{
            System.out.println("\n==== Mostrar Libros. ====");
            System.out.print("""
                Como deseas buscar los libros.
                1.Listar por autor.
                2.Buscar por ISBN.
                3.Regresar al inicio.
                Ingrese su opción:\s""");
            int opcion = sc.nextInt();
            sc.nextLine();
            switch(opcion){
                case 1 -> buscarAutor();
                case 2 -> buscarISBN();
                case 3 -> System.out.println("Regresando al menu inicial.");
                default -> System.out.println("Ingrese una opción correcta.");
            }
        }catch (InputMismatchException tipo){
            System.out.println("Ingrese datos correctos.");
        }
    }

    public void buscarAutor(){
        List<Libro> listaLibro;
        System.out.print("Nombre del autor: ");
        String autor = sc.nextLine();

        listaLibro = consultaLibros.listarPorAutor(autor);

        if(listaLibro.isEmpty()){
            //Si la lista esta vacia entonces regresamos y mostramos una alerta.
            System.out.println("El autor " + autor + " no tiene libros guardados." );
        }else{
            listaLibro.forEach(libro -> {
                System.out.println(libro.mostrarDatos());
            });
        }

    }

    public void buscarISBN(){
        Libro libro = null;
        System.out.println("Obtener libro por ISBN");
        System.out.print("ISBN del libro: ");
        String ISBN = sc.nextLine();
        libro = consultaLibros.obtenerLibro(ISBN);
        if(libro != null){
            //Si libro es diferente a null entonces mostramos los datos del libro.
            System.out.println(libro.mostrarDatos());
        }else{
            System.out.println("No se encontro el libro.\ningrese un ISBN correcto");
        }
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
            System.out.println("Libro con ISBN existente.");
            return false;
        }

        System.out.print("Titulo: ");
        String titulo = sc.nextLine().trim();
        System.out.print("Categoria: ");
        String categoria = sc.nextLine().trim();
        System.out.print("Año de salida: ");
        String year = sc.nextLine().trim();
        if(year.length() < 4 ){
            System.out.println("Ingrese una año como: (2020) con 4 dígitos");
            return false;
        }
        System.out.print("Autor: ");
        String autor = sc.nextLine().trim();

        //Generamos él prefijó, el cual en este caso será realmente grande.
        String prefijo = (categoria.substring(0,2) + autor.substring(0,2)+ titulo.substring(0, 2) + year.substring(2, 4)).toUpperCase();
        //Ahora guardamos los valores en la base de datos, sabiendo que no hay ISBN repetidos.
        boolean agregado = consultaLibros.setLibro(ISBN, titulo, categoria, autor, prefijo);
        if(agregado){
            //Si se agregó el libro entonces procedemos con la función de cantidad de ejemplares.
            int cantidad = agregarEjemplares(prefijo, ISBN);
            if(cantidad > 0){
                //Si la cantidad es mayor a cero entonces se guardó al menos 1.
                acceso = true;
            }
        }
        return acceso;
    }

    //======================| AGREGAR EJEMPLARES. |===========================
    //Agregar ejemplares cuando se crea un nuevo Libro.
    public int agregarEjemplares(String prefijo, String ISBN){
        boolean agregado;
        int contador = 0;
        try{
            System.out.print("Ingresa la ubicación del libro: ");
            String ubicacion = sc.nextLine().trim();
            System.out.print("Tipo de libro(Virtual o físico): ");
            String tipo = sc.nextLine().trim();
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
        return contador;
    }

    //Agrega ejemplares a ya los existentes.
    public void agregarMasEjemplares(){
        try{
            EjemplarDAO ejemplar;
            System.out.print("\nIngrese el prefijo del ejemplar: ");
            String prefijo = sc.nextLine();
            ejemplar = consultaEjemplar.ultimoEjemplar(prefijo);
            if(ejemplar != null){
                //Si ejemplar es diferente a null entonces mostramos los datos y le decimos al usuario si realmente quiere agregar mas ejemplares.
                System.out.println(ejemplar.mostrarDatos());
            }else{
                System.out.println("No se encontro el ejemplar con el ISBN ingresado.");
                return;
            }
            System.out.println("""
                ¿Deseas agregar mas ejemplares de este libro?
                1.Agregar mas.
                2.Regresar al inicio.
                Ingrese su opción:\s""");
            int opc = sc.nextInt();
            if(opc == 1){
                datosEjemplar(prefijo);
            }else{
                System.out.println("Regresando al menu inicial.");
            }
        }catch(InputMismatchException tipos){
            System.out.println("Ingrese correctamente los datos que le solicitan.");
        }
    }

    public void datosEjemplar(String prefijo){
        Ejemplar ejemplar;
        String ultimo = consultaEjemplar.ultimoID(prefijo);
        //Ahora teniendo el ID con el último número toca obtener ese número.
        int inicio = ultimo.lastIndexOf("_");
        int longitud = ultimo.length();
        //Obtenemos el último número de los ejemplares.
        int numero = Integer.parseInt(ultimo.substring((inicio + 1), longitud));
        System.out.println("El numero completo es: " + ultimo.substring((inicio + 1), longitud) + ", resumido es: " + numero );
        //Ahora teniendo ya el número toca
        String ID = generarID.generarID(prefijo, (numero + 1));
        //Ahora toca realizar el bucle para guardar los datos.

    }

    //======================| MOSTRAR EJEMPLARES. |===========================
    public void mostrarEjemplares(){
        Ejemplar ejemplar;
        System.out.println("Buscar ejemplar por ID.");
        System.out.print("Ingrese el ID: ");
        String ID = sc.nextLine();

        //Ahora realizamos la consulta
        ejemplar = consultaEjemplar.obtenerEjemplar(ID);
        if(ejemplar != null){
            //Si no se recibieron valores null entonces mostramos datos.
            System.out.println(ejemplar.mostrarDatos());
        }else{
            System.out.println("No se encontro el ejemplar ingresado.");
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

    //======================| BUCLES PARA AGREGAR Y MOSTRAR LIBROS Y EJEMPLARES. |===========================
    public void bucleMostrarISBN(){
    }
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
