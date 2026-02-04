package Servicios;

import DAO.PersonaDaoImpl;
import Entidades.Usuarios.Persona;
import Exceptions.CorreoNoValido;
import Exceptions.UsuarioNoEncontrado;
import Interfaces.PersonaDAO;
import Utilidades.GenerarID;
import Utilidades.Login;
import Utilidades.OcultarClaves;
import Utilidades.ValidarCorreo;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * En esta clase se realizarán las diferentes funcionalidades de los usuarios como
 * menus, obtener información, etc.
 *
 */
public class UsuariosServicio {
    private final Scanner sc = new Scanner(System.in);
    private final PersonaDAO consultas = new PersonaDaoImpl();
    ValidarCorreo validarCorreo = new ValidarCorreo();
    GenerarID generarID = new GenerarID();
    OcultarClaves ocultarClaves = new OcultarClaves();
    Login log = new Login();

    public void menuPersonas() {
        try {
            System.out.print("""
                    \nBienvenido a la sección personas.
                    ¿Qué acción deseas realizar?
                    1.Acceder a Bibliotecarios.
                    2.Acceder a Usuarios.
                    3.Regresar al inicio.
                    Ingresa tu opción:\s""");
            int opcMenu = sc.nextInt();
            switch (opcMenu) {
                case 1 -> menuBibliotecarios();
                case 2 -> menuUsuarios();
                case 3 -> System.out.println("Regresando al menu principal.");
                default -> System.out.println("Ingrese una opción correcta.");
            }
        } catch (InputMismatchException tipo) {
            System.out.println("Ingrese un valor correcto por favor");
        }
    }

    //======================| MENU BIBLIOTECARIOS |===========================

    /**
     * La siguiente función {@link #menuBibliotecarios()} es un menu muy similar a {@link #menuUsuarios()}, ya que solo
     * se utiliza la función {@link #mensajeMenu(String)} para asi poder eliminar try-catch por errores de entrada o
     * tipos de dato, metiendo en un mensaje las diferentes opciones que se pueden realizar.
     */
    public void menuBibliotecarios() {
        int opcion = mensajeMenu("Bibliotecarios");
        sc.nextLine();
        boolean acceder;
        switch (opcion) {
            case 1 -> buscarPersona("Bibliotecario");
            case 2 ->{
                acceder = log.login();
                if(acceder){
                    agregarPersona("Bibliotecario");
                }
            }
            case 3 -> System.out.println("Regresando al inicio.");
            default -> System.out.println("Ingrese una opcion correcta.");
        }
    }

    // =====================| MENU USUARIOS |=========================
    /// La siguiente función se explica en: {@link #menuBibliotecarios()}
    public void menuUsuarios() {
        boolean acceder;
        int opcion = mensajeMenu("Usuarios");
        sc.nextLine();
        switch (opcion) {
            case 1 -> buscarPersona("Usuario");
            case 2 -> {
                acceder = log.login();
                if(acceder) {
                    agregarPersona("Usuario");
                }else{
                    System.out.println("Para acceder a esta sección, necesitas ingresar datos correctos.");
                }
            }
            case 3 -> System.out.println("Regresando al inicio.");
            default -> System.out.println("Ingrese una opcion correcta.");
        }
    }

    // =====================| BUSCAR PERSONAS |=========================

    /**
     * Esta función sirve para realizar una búsqueda mediante el ID ingresado, la diferencia de esta función es que
     * al recibir por parametro {@code area} filtramos en que el ID ingresado deberá de ser de esa areá especifíca.
     * Se pueden recibir dos valores de la base de datos que son: <br>
     * * El primero es un objeto del tipo {@code Persona} en donde
     *      se llama a la función {@code mostrarDatos()} del objeto para ver los datos del usuario. <br>
     * * El segundo es un objeto de la clase {@code Persona} pero con un valor null, del cual mostraremos un mensaje como
     * usuario no encontrado
     * @param area : Se pueden recibir valores como {@code "Bibliotecario"} o {@code "Usuario"}, este sirve como filtro
     *             al momento de buscar en la base de datos.
     */
    public void buscarPersona(String area) {
        try {
            Persona persona;
            System.out.print("\nIngrese el ID del " + area + ": ");
            String ID = sc.nextLine();
            //Este if es para validar que el ID que ingresa es con respecto al areá que selecciono,
            //por ejemplo si selecciono la areá usuarios no puede ingresar un ID de bibliotecarios.
            if (ID.contains(area.toUpperCase().substring(0, 3))) {
                persona = consultas.obtenerPersona(ID);
                //Valída que los datos del usuario estén correctos.
                if (persona != null) {
                    System.out.println(persona.mostrarDatos() + "\n");
                }
            } else {
                System.out.println("Ingrese el ID de un " + area + "\n");
            }
        } catch (UsuarioNoEncontrado usuarioNo) {
            System.out.println(usuarioNo.getMessage());
        } catch (InputMismatchException tipo) {
            System.out.println("Ingrese los valores que le solicitan.");
        }
    }

    // =====================| AGREGAR PERSONAS |=========================

    /**
     * Esta función es la más importante y necesaria para guardar a nuevos usuarios. <br>
     * Primeramente, se muestran instrucciones básicas y se solicitan datos necesarios para la base de datos. <br><br>
     * Al ingresar el correo tenemos dos validaciones necesarias que son: <br>
     *
     * *{@link #validarCorreo} : Esta funcionalidad proviene de la clase {@code validarCorreo} en donde validamos
     *  si el correo cuenta con un {@code "@"} y termina en {@code ".com"}, si se ingresa un correo inválido entonces
     *  dejamos de proceder con el guardado de datos. <br>
     * *{@code correoExistente(String)} : Esta funcionalidad proviene de la clase {@link #consultas} en donde la función
     * correoExistente realiza una búsqueda de ese correo en la base de datos y si encuentra alguna coincidencia no lo
     * agrega y muestra una alerta de usuario. <br> <br>
     *
     * Ahora se debe de aclarar que los usuarios no pueden tener una contraseña, ya que el bibliotecario es el encargado
     * de prestar y realizar la aceptación de un  regreso de libros, por lo que el bibliotecario debe de tener una contraseña
     * en este caso si ingresa un bibliotecario se podrá preguntar por una contraseña para los nuevos bibliotecarios. <br><br>
     *
     * Cuando se termine de ingresar los datos del nuevo usuario entonces se realizará una consulta a base de datos para
     * saber cuál es el último ID basándonos en el área ingresada, con base a esto se generará el ID del nuevo Usuario o
     * Bibliotecario ya sea en la última posición o iniciándola desde cero.<br> <br>
     * Ahora si después de todas las validaciones realizamos él {@code INSERT} en base de datos.
     *
     * @param area :
     */
    public void agregarPersona(String area) {

        try {
            System.out.println("\nEn esta sección se agregaran " + area);
            System.out.println("Ingrese los datos que le solicitan a continuación");
            String clave = "";
            String prefijo = area.toUpperCase().substring(0, 3);
            String prestamo = (area.equals("Bibliotecario")) ? "no admite" : "Sin prestamos";
            String ID;

            System.out.print("Nombres: ");
            String nombre = sc.nextLine().trim();
            System.out.print("Apellidos: ");
            String apellido = sc.nextLine().trim();
            System.out.print("Correo electronico: ");
            String correo = sc.nextLine().trim();

            //Esta condicional válida si el correo está bien escrito.
            if (!validarCorreo.validarCorreo(correo)) {
                return;
            }
            //Sa siguiente validacion es para saber si el correo ya está registrado.
            if (consultas.correoExistente(correo)) {
                //Si me regresa un true entonces le decimos al usuario que ya existe un correo asi.
                System.out.println("Correo electronico existente, agregue uno diferente.");
                return;
            }

            //Si el usuario es un bibliotecario entonces preguntamos por una contraseña.
            if (area.equals("Bibliotecario")) {
                //Si es igual a bibliotecario entonces pedimos una clave o contraseña.
                System.out.print("Contraseña: ");
                clave = sc.nextLine().trim();
                clave = ocultarClaves.ocultarContras(clave);
            }

            //Generamos el ID del usuario
            String valor = consultas.obtenerUltimo(prefijo);

            //Realizamos la generación del ID
            if (valor != null) {
                //Si el valor obtenido es diferente a null significa que existe algún valor.
                int numero = Integer.parseInt(valor.substring(4, 8));
                ID = generarID.generarID(prefijo, (numero + 1));

            } else {
                //Si el valor es null significa que no existen registros realizados en esa área, por lo que empezamos en 1
                ID = generarID.generarID(prefijo, 1);
            }
            //Ahora si se guardan los datos, pero se separan los roles de usuario
            consultas.agregarPersona(ID, nombre, apellido, correo, clave, area, prestamo);
        } catch (InputMismatchException tipo) {
            System.out.println("Ingrese los valores que le solicitan");
        } catch (CorreoNoValido correo) {
            //Error por si el correo no es valido.
            System.out.println(correo.getMessage());
        }

    }

    // =====================| FUNCIONES REPETIBLES |=========================

    /**
     * Esta función está hecha para no repetir el mensaje del menu del areá ya sea {@code "Bibliotecarios"}
     * o {@code "Usuarios"}, también solicita la opción que se requiera y asi dirigir al usuario a la sección que ocupa
     * @param area : Aquí se pasa en que area se solicita el menu ya sea {@code "Bibliotecarios"} o {@code "Usuarios"}.
     * @return : Regresará un valor entero con respecto a la opción realizada por el usuario.
     */
    public int mensajeMenu(String area) {
        int opcion = 0;
        try {
            System.out.println("\nBienvenido a la sección " + area);
            System.out.println("¿Qué acción desear realizar?");
            System.out.println("1.Mostrar " + area);
            System.out.println("2.Agregar " + area);
            System.out.println("3.Regresar al inicio.");
            System.out.print("Ingrese la opción: ");
            opcion = sc.nextInt();

        } catch (InputMismatchException tipo) {
            System.out.println("Ingrese una opción correcta.");
        }
        return opcion;
    }

}
