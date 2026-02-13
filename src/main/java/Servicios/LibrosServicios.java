package Servicios;

import DAO.EjemplarDaoImpl;
import DAO.LibroDaoImp;
import Entidades.Ejemplar;
import Entidades.EjemplarDAO;
import Entidades.Libro;
import Interfaces.EjemplarDao;
import Interfaces.LibroDAO;
import Utilidades.GenerarID;
import Utilidades.Login;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/// En esta sección estarán las diferentes funcionalidades de Libros y Ejemplares.
public class LibrosServicios {
    private final Scanner sc = new Scanner(System.in);
    LibroDAO consultaLibros = new LibroDaoImp();
    EjemplarDao consultaEjemplar = new EjemplarDaoImpl();
    GenerarID generarID = new GenerarID();
    Login login = new Login();

    ///  Esta función sirve para mostrar el menu de Libros y ejemplares.
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
    ///  Este menu servirá para dirigir la opcion ingresada ya sea a buscar libros o ingresar libros. <br>
    ///Al igual que {@link #menuEjemplares()}
    public void menuLibros(){
        int opcion = mensajeMenu("Libros");
        sc.nextLine();
        switch(opcion){
            //Opción mostrar Libros
            case 1 -> obtenerLibro();
            //Opción agregar Libros
            case 2 -> {
                boolean log = login.login();
                if(log){
                bucleLibros();
                }
            }
            case 3 -> System.out.println("Regresando al menu inicial.");
            default -> System.out.println("Ingrese una opción correcta");
        }
    }

    //======================| MENU EJEMPLARES. |===========================
    ///  Este menu sirve para dirigir la opción ingresada ya sea a {@link #mostrarEjemplares()} o {@link #agregarMasEjemplares()}
    public void menuEjemplares(){
        int opcion = mensajeMenu("Ejemplares");
        sc.nextLine();
        switch(opcion){
            //Opción mostrar Ejemplares
            case 1 -> mostrarEjemplares();
            //Opción agregar Ejemplares
            case 2 ->{
                boolean log = login.login();
                if(log) {
                    agregarMasEjemplares();
                }
            }
            //Opción para editar Ejemplares
            case 3 -> menuEditar();
            case 4 -> System.out.println("Regresando al menu inicial.");
            default -> System.out.println("Ingrese una opción correcta");
        }
    }

    //======================|  MOSTRAR LIBROS. |===========================

    /**
     * Esta opción es la primera para mostrar libros, podemos encontrar <br>
     * <b>{@link #buscarAutor()} </b> : Con esta función listamos todos los libros guardados del autor. <br>
     * <b>{@link #buscarISBN()}</b> : Con esta función obtenemos un libro mediante el ISBN.
     */
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

    /**
     * Esta función es la que listara los libros del autor. <br>
     * Aquí contamos con una validación, si la lista está vacía entonces mostramos un mensaje basico.
     */
    public void buscarAutor(){
        List<Libro> listaLibro;
        System.out.print("Nombre del autor: ");
        String autor = sc.nextLine();

        listaLibro = consultaLibros.listarPorAutor(autor);

        if(listaLibro.isEmpty()){
            //Si la lista esta vacia entonces regresamos y mostramos una alerta.
            System.out.println("No tenemos libros guardados de: " + autor);
            return;
        }
        int cantidad = listaLibro.size();
        System.out.println("\nContamos con " + cantidad + " de títulos del autor: " + autor);
        listaLibro.forEach(libro -> {
            System.out.println(libro.mostrarDatos());
        });
    }

    /// Esta función sirve para mostrar un Libro mediante el ISBN,
    /// Si el usuario no existe entonces muestra un a validación simple para saber si el objeto es null o no.
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
    /// Esta función sirve para agregar nuevos libros.
    public boolean agregarLibros(){
        boolean acceso = false;
        System.out.print("""
                \nAgregar libros.
                Ingrese los datos que se le solicitan.
                El año del libro deberá ser ingresada por completo (2020)
                """);
        System.out.print("ISBN del libro: ");
        String ISBN = sc.nextLine();
        //1. Validá que el libro exista.
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
        //2. Validá que el año establecido se contenga al menos 4 caracteres
        if(year.length() != 4){
            System.out.println("Ingrese una año como: (2020) con 4 dígitos");
            return false;
        }
        System.out.print("Autor: ");
        String autor = sc.nextLine().trim();

        //Generamos él prefijó, el cual en este caso será realmente grande.
        String prefijo = (categoria.substring(0,2) + autor.substring(0,2)+ titulo.substring(0, 2) + year.substring(2, 4)).toUpperCase();
        //Ahora guardamos los valores en la base de datos, sabiendo que no hay ISBN repetidos.
        boolean agregado = consultaLibros.setLibro(ISBN, titulo, categoria, autor, prefijo);
        //3. Valida que el Libro se guardo, si se guardo entonces procedemos con el guardado de datos
        if(agregado){
            //Si se agregó el libro entonces procedemos con la función de cantidad de ejemplares.
            agregarEjemplares(prefijo, ISBN);
        }
        return acceso;
    }

    //======================| AGREGAR EJEMPLARES. |===========================

    /**
     * Esta función sirve para obtener los datos de los ejemplares que se agregaran al instante de agregar un Libro.
     * @param prefijo : El prefijo será obtenido por {@code categoria}, {@code autor} y {@code Titulo} obteniendo dos caracteres de cada atributo.
     * @param ISBN : ISBN del libro.
     */
    public void agregarEjemplares(String prefijo, String ISBN){
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
    }

    /**
     * Esta función sirve para agregar más ejemplares, esta es diferente a {@link #agregarEjemplares(String, String)}, ya que
     * en esta agregamos ejemplares sobre los ejemplares existentes, por lo que ya no se obtendran los datos del ejemplar,
     * ya que se reutilizaran los atributos de los existentes.
     */
    public void agregarMasEjemplares(){
        try{
            EjemplarDAO ejemplar;
            System.out.print("\nIngrese el prefijo del ejemplar: ");
            String prefijo = sc.nextLine();
            ejemplar = consultaEjemplar.ultimoEjemplar(prefijo);
            //1. Validá que exista el ejemplar.
            if(ejemplar != null){
                System.out.println("El ultimo ID es: " + ejemplar.getID());
                //Si ejemplar es diferente a null entonces mostramos los datos y le decimos al usuario si realmente quiere agregar mas ejemplares.
                System.out.println(ejemplar.mostrarDatos());
            }else{
                System.out.println("No se encontró el ejemplar con el ISBN ingresado.");
                return;
            }
            System.out.print("""
                \n¿Deseas agregar mas ejemplares de este libro?
                1.Agregar mas.
                2.Regresar al inicio.
                Ingrese su opción:\s""");
            int opc = sc.nextInt();
            if(opc == 1){
                //2. Esta función es la que agrega los datos de los nuevos ejemplares.
                datosEjemplar(ejemplar, prefijo);
            }else{
                System.out.println("Regresando al menu inicial.");
            }
        }catch(InputMismatchException tipos){
            System.out.println("Ingrese correctamente los datos que le solicitan.");
        }
    }

    /**
     * Esta función es para obtener los datos del ejemplar.
     * @param ejemplar : Objeto que tiene los datos fundamentales del ejemplar.
     * @param prefijo : Con este prefijo podemos hacer un ID nuevo para los ejemplares.
     */
    public void datosEjemplar(EjemplarDAO ejemplar, String prefijo){

        String ultimo = ejemplar.getID();
        //Ahora teniendo el ID con el último número toca obtener ese número.
        int inicio = ultimo.lastIndexOf("_");
        int longitud = ultimo.length();
        //Obtenemos el último número de los ejemplares.
        int numero = Integer.parseInt(ultimo.substring((inicio + 1), longitud));
        //Ahora teniendo ya el último número realizamos el bucle, creando ais un nuevo ID
        String ID = (generarID.generarID(prefijo, (numero + 1))).toUpperCase();
        //Ahora toca realizar el bucle para guardar los datos.
        bucleAgregarMas(ejemplar, numero, prefijo);
    }

    /**
     * Esta función es la que realizara el guardado de datos.
     * @param ejemplar : Objeto que contiene datos fundamentales.
     * @param numero : Ultimo numero del ultimo ID
     * @param prefijo : El prefijo del ejemplar.
     */
    public void bucleAgregarMas(EjemplarDAO ejemplar, int numero, String prefijo){
        try{
            String ISBN = ejemplar.getISBN();
            String ubicacion = ejemplar.getUbicacion();
            String tipo = ejemplar.getTipo();
            boolean guardado;
            int contador = 0;
            System.out.print("Ingrese la cantidad de ejemplares: ");
            int cantidad = sc.nextInt();
            for(int i = 1; i <= cantidad; i++){
                String ID = (generarID.generarID(prefijo, (numero + i))).toUpperCase();
                guardado = consultaEjemplar.setEjemplar(ID, ISBN, ubicacion, tipo);
                if(guardado){
                    contador++;
                }
            }
            System.out.println("Se guardaron " + contador + " de " + cantidad + " ejemplares.");
        }catch(InputMismatchException tipo){
            System.out.println("Ingrese los datos que le solicitan");
        }
    }

    //======================| EDITAR EJEMPLARES. |===========================
    //Lo unico que será editado son los datos fundamentales, como ubicacion y tipo

    public void menuEditar(){
        System.out.print("""
                ¿Que datos deseas editar?
                1.Ubicación.
                2.Tipo.
                3.Salir
                Ingrese su opción:\s""");
        int opcion = sc.nextInt();
        switch(opcion){
            case 1 -> editarUbicacion();
            case 2 -> editarTipo();
            case 3 -> System.out.println("Regresando al menu inicial.");
            default -> System.out.println("Ingrese una opción correcta.");
        }

    }
    public int mensajeEditar(){
        int cantidad = 0 ;
        try{
            System.out.print("Cuantos ejemplares editara: ");
            cantidad = sc.nextInt();
            sc.nextLine();
        }catch(InputMismatchException tipo ){
            System.out.println("Ingrese los datos que se le solicitan.");
        }
        return cantidad;
    }

    //Prueba este code;
    public void editarUbicacion(){
        List<String> lista = listaEjemplares();
        boolean actualizado = false;
        //Si la lista está vacía entonces regresamos con un mensaje simple.
        if(lista.isEmpty()){
            System.out.println("La lista no tiene ejemplares a editar.");
            return;
        }
        //Como en esta función se editara la ubicación es necesario que el usuario ingrese la nueva ubicacion.
        int limite = lista.size();
        for(int i = 0; i < limite; i++){
            System.out.print("Ingrese la nueva ubicación del ejemplar " + lista.get(i) + ": ");
            String nuevaUbi = sc.nextLine();
            actualizado = consultaEjemplar.editUbicacion(nuevaUbi, lista.get(i));
            if(actualizado){
                System.out.println("Se actualizo correctamente el ejemplar: " + lista.get(i));
            }else{
                System.out.println("No se actualizo el ejemplar.");
            }
        }

    }
    public void editarTipo(){
        List<String> lista = listaEjemplares();
        boolean actualizado = false;
        //Si la lista está vacía entonces regresamos con un mensaje simple.
        if(lista.isEmpty()){
            System.out.println("La lista no tiene ejemplares a editar.");
            return;
        }
        System.out.println("Se cambiara el tipo de libro automáticamente." +
                "\nSi es físico cambiara a Digital y viceversa.");
        int limite = lista.size();
        for(int i = 0; i < limite; i++){
            String tipoLibroBD = consultaEjemplar.obtenerTipo(lista.get(i));
            System.out.println("El ejemplar es: " + tipoLibroBD);
            String tipoNuevo = (tipoLibroBD.equals("Físico")) ? "Digital" : "Físico" ;
            actualizado = consultaEjemplar.editTipo(tipoNuevo, lista.get(i));
            if(actualizado){
                //Si me regresa un atualizado entonces regresamos el mensaje de alerta.
                System.out.println("Se actualizo correctamente el ejemplar: " + lista.get(i) + " ahora es: " + tipoNuevo);
            }else{
                System.out.println("No se actualizo el ejemplar.");
            }
            //Realizamos la consulta

        }

    }

    public List<String> listaEjemplares(){
        int cantidad = mensajeEditar();
        boolean existe = false;
        int contador = 0;
        List<String > listEjemplares = new ArrayList<>();
        if(cantidad == 0){
            System.out.println("\nIngrese una cantidad valida de ejemplares.");
            return listEjemplares;
        }
        for(int i = 0; i < cantidad; i++){
            System.out.print("Ingrese el ID del libro: ");
            String ID = sc.nextLine().trim().toUpperCase();
            existe = consultaEjemplar.existeEjemplar(ID);
            //Al realizar la consulta regresará un valor booleano en el cual sabremos si validaremos su existencia en la lista.
            if(existe){
                System.out.println("Libro existente");
                //Este if busca en la lista si existe alguna coincidencia mediante el valor del ID.
                if(!listEjemplares.contains(ID)){
                    System.out.println("Se guardara el ejemplar " + ID);
                    //Si la lista no tiene ninguna coincidencia entonces agregamos el valor
                    listEjemplares.add(ID);
                    contador++;
                }else{
                    System.out.println("El ejemplar " + ID + " se guardo para editar.");
                }
            }else{
                System.out.println("El ejemplar con el ID " + ID + " no existe.");
            }
        }
        System.out.println("Se editaran " + contador + " de " + cantidad + " ejemplares solicitados.");
        System.out.println("Los libros agregados son: ");
        listEjemplares.forEach(System.out::println);
        return listEjemplares;
    }

    //======================| MOSTRAR EJEMPLARES. |===========================

   /// Función que realiza la búsqueda de ejemplares mediante el ID.
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

    /// Mensaje genérico para las secciones de menu
    public int mensajeMenu(String area){
        int opcion = 0;
        int numero = (area.equals("Ejemplares"))? 4 : 3;
        try{
            System.out.println("\nBienvenido a la sección " + area);
            System.out.println("¿Que deseas realizar?");
            System.out.println("1.Buscar " + area);
            System.out.println("2.Agregar " + area);
            //Si es igual a ejemplares entonces podrá editarse datos fundamentales menos disponibilidad.

            if(area.equals("Ejemplares")){System.out.println("3.Editar " + area);}
            System.out.println( numero + ".Regresar al inicio");
            System.out.print("Ingresa tu opción: ");
            opcion = sc.nextInt();
        }catch(InputMismatchException tipo){
            System.out.println("Ingrese los datos que se le solicitan.");
        }
        return opcion;
    }

    //======================| BUCLES PARA AGREGAR Y MOSTRAR LIBROS Y EJEMPLARES. |===========================
    public void bucleLibros(){
        boolean continuar;
        for(int i = 0; i < 3; i++){
            continuar = agregarLibros();
            if(continuar){
                //Si continuar es true significa que se guardó el libro correctamente, por lo que debemos terminar el bulge.
                break;
            }else{
                System.out.println("Estas en el intento " + ( i + 1) + ", el limite es: 3");
            }
        }
    }

}
