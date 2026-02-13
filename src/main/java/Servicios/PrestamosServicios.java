package Servicios;

import DAO.EjemplarDaoImpl;
import DAO.PersonaDaoImpl;
import DAO.PrestamoDaoImpl;
import Entidades.Ejemplar;
import Entidades.PrestamoDao;
import Entidades.Usuarios.Persona;
import Entidades.Usuarios.Usuario;
import Entidades.Usuarios.UsuarioDTO;
import Exceptions.CorreoNoValido;
import Interfaces.EjemplarDao;
import Interfaces.PersonaDAO;
import Interfaces.PrestamoDAO;
import Utilidades.GenerarID;
import Utilidades.Login;
import Utilidades.ValidarCorreo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PrestamosServicios {

    Scanner sc = new Scanner(System.in);
    EjemplarDao consultaEjemplar = new EjemplarDaoImpl();
    PersonaDAO consultaPersona = new PersonaDaoImpl();
    PrestamoDAO consultaPrestamo = new PrestamoDaoImpl();
    GenerarID generarID = new GenerarID();
    ValidarCorreo validarCorreo = new ValidarCorreo();
    Login login = new Login();

    ///  Esta función sirve para mostrar un menu sobre que secciones se pueden abrir.
    public void menuPrestamo() {
        try {
            System.out.println("""
                    Sección de prestamos.
                    ¿Qué acción deseas hacer?
                    1.Buscar préstamo.
                    2.Realizar un préstamo.
                    3.Regresar libro.
                    4.Regresar al menu inicial.
                    Ingrese su opción:\s""");
            int opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1 -> menuBuscar();
                case 2 -> {
                    boolean bucle = login.login();
                    if(bucle){
                        realizarPrestamo();
                    }
                }
                case 3 -> {
                    boolean bucle = login.login();
                    if(bucle) {
                        regresarLibro();
                    }
                }
                case 4 -> System.out.println("Regresando el menu inicial.");
                default -> System.out.println("Ingrese una opción valida.");
            }
        } catch (InputMismatchException tipo) {
            System.out.println("Ingrese los datos correctamente.");
        }
    }

    //======================| REALIZAR PRÉSTAMO (insert) |================

    /**
     * Esta función sirve para obtener datos fundamentales del prestamo como el correo del usuario, el ID del ejemplar
     * generador de ID del prestamo, fecha de entrega y de prestamo.
     */
    public void realizarPrestamo() {
        try {
            Persona persona;
            Ejemplar ejemplar;
            System.out.println("Realizar Prestamos" +
                    "\nIngrese los datos que se le solicitan.");

            System.out.print("Correo del usuario: ");
            String correo = sc.nextLine();

            persona = consultaPersona.correoPersona(correo);
            boolean validarUsu = validarUsuario(persona);
            //1. Validamos que el usuario exista o no sea un bibliotecario.(Solo usuarios)
            if (!validarUsu) {
                //Si regresa un false entonces retornamos
                System.out.println("Usuario no encontrado o el usuario es un bibliotecario.");
                return;
            }
            //Por esto necesitamos que sean solo Usuarios, ya que si no habría problemas.
            Usuario usuario = (Usuario) persona;
            String prestamo = usuario.getLibroPrestado();
            //2.Validamos que el usuario no tenga préstamos
            if (!prestamo.equals("Sin prestamos")) {
                //Si es diferente entonces regresamos y no le prestamos a un usuario con deuda
                System.out.println("El usuario ya se le presto un libro.");
                return;
            }
            //Como ya se realízo el filtro entonces toca validar la existencia del ejemplar
            System.out.print("ID del ejemplar: ");
            String ejemplarID = sc.nextLine().toUpperCase();
            ejemplar = consultaEjemplar.obtenerEjemplar(ejemplarID.toUpperCase());

            //3. Validamos que el ejemplar exista
            if (ejemplar == null || !ejemplar.getDisponible()) {
                //Si es igual a null entonces regresamos al inicio
                System.out.println("Ejemplar no encontrado o no disponible");
                return;
            }

            //Ahora como los datos están bien validamos toca obtener la fecha del préstamo y un límite para
            //la fecha de entrega.
            //Dia del préstamo
            LocalDate fechaActual = LocalDate.now();
            String fechaRegreso = String.valueOf(fechaActual.plusWeeks(2));

            System.out.println("\nSe prestara el ejemplar " + ejemplar.mostrarDatos() +
                    "\nAl usuario: " + persona.mostrarDatos());
            //Obtenemos el ID del prestamo
            String ultimoID = consultaPrestamo.ultimoPrestamo();
            String prestamoID = obtenerID(ultimoID);
            validarPrestamo(prestamoID, ejemplarID, usuario.getPersonaID(), String.valueOf(fechaActual), fechaRegreso);
        } catch (CorreoNoValido correo) {
            System.out.println(correo.getMessage());
        }
    }

    /**
     * Esta función sirve para establecer un ID mediante el último ID en la base de datos.
     * @param ultimoID : ID obtenido desde la base de datos, este sera el ultimo agregado.
     * @return Regresará una cadena con el ID del préstamo.
     */
    public String obtenerID(String ultimoID) {
        //1. Si ID es null entonces regresamos un valor fijo.(if)
         if (ultimoID == null) return generarID.generarID("PREST", 1);

        //2. Si ultimoID no es null entonces regresamos un ID compuesto
        int numeroComple = ultimoID.indexOf("_");
        String IDprest = ultimoID.substring((numeroComple + 1));
        int numero = Integer.parseInt(IDprest);
        return generarID.generarID("PREST", numero + 1);
    }

    /**
     * Esta función validará la que el prestamo se realizó correctamente.
     * @param prestamoID : ID que se asignará al préstamo,
     * @param ejemplarID : Ejemplar que se asignara al préstamo.
     * @param usuarioID : Usuario que se asignara al préstamo.
     * @param fechaActual : Fecha en el que se présto el ejemplar
     * @param fechaRegreso : Fecha en el que el ejemplar se tendrá que entregar el ejemplar.
     */
    public void validarPrestamo(String prestamoID, String ejemplarID, String usuarioID, String fechaActual, String fechaRegreso) {
        //1. Realizamos el insert del préstamo
        boolean prestamoIn = consultaPrestamo.setPrestamo(prestamoID, ejemplarID, usuarioID, fechaActual, fechaRegreso);
        //2. Validamos que el prestamo se realizó, en est caso si no se realizo mostramos una alerta y regresamos.
        if (!prestamoIn) {
            System.out.println("Préstamo no realizado. Regresando.");
            return;
        }
        //3. Realizamos la consulta para cambiar de estado del ejemplar.
        boolean actualizarEjemplar = consultaEjemplar.cambiarEstado(ejemplarID);
        //4. Validamos si cambio, si no cambio eliminamos el prestamo y regresamos al inicio.
        if (!actualizarEjemplar) {
            //Si me regresa un false entonces regresamos y no prestamos el ejemplar;
            System.out.println("No se pudo actualizar el estado del libro.");
            //4.1. Como no se realizo la consulta entonces borramos el prestamo.
            boolean eliminarPre = consultaPrestamo.borrarPrestamo(prestamoID);
            if (eliminarPre) {
                System.out.println("Se elimino correctamente el préstamo.");
            }
            return;
        }
        //5. Realizamos la consulta que actualice el estado de la persona.
        boolean actualizarUsuario = consultaPersona.prestarEjemplar(usuarioID, ejemplarID);
        //5.1. Validamos que el usuario se actualizó si no realizamos dos acciones más
        if (!actualizarUsuario) {
            System.out.println("No se pudo guardar el ejemplar en el usuario");

            //5.2.1 Realizamos la consulta para cambiar de estado al ejemplar.
            actualizarEjemplar = consultaEjemplar.cambiarEstado(ejemplarID);
            //5.2.2. Validamos que el ejemplar si cambio al estado incial.
            if (actualizarEjemplar) {
                System.out.println("El ejemplar regreso a su estado inicial.");
            }
            //5.3.1 Realizamos la consulta para borrar el prestamo que no se completó
            boolean eliminarPre = consultaPrestamo.borrarPrestamo(prestamoID);
            //5.3.2 Validamos que el prestamo se borró
            if (eliminarPre) {
                System.out.println("Se elimino correctamente el préstamo.");
            }
        }
        System.out.println("Se realizo el préstamo correctamente.");
    }


    /**
     * Esta función validará para empezar si el objeto no es null y si es un usuario.
     * @param persona : Objeto de la clase persona.
     * @return Si es un bibliotecario regresara un false, si es un usuario regresara un true.
     */
    public boolean validarUsuario(Persona persona) {
        if (persona == null) {
            return false;
        }
        //Si es un bibliotecario entonces regresara un false
        return !persona.getPersonaID().contains("BIB");
    }


    //======================| BUSCAR PRÉSTAMO (select) |================

    /**
     * Esta función es el menu para poder acceder a los datos de los préstamos,
     * en donde podemos buscar préstamos por ID y por correo de usuario.
     */
    public void menuBuscar() {
        try {
            System.out.print("""
                    ¿Como deseas buscar el préstamo?
                    1.Correo del usuario.
                    2.Por ID.
                    3.Regresar al menu inicial.
                    Ingrese su opción:\s""");
            int opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1 -> buscarCorreo();
                case 2 -> buscarPorID();
                case 3 -> System.out.println("Regresando al menu inicial.");
                default -> System.out.println("Ingrese una opción valida");
            }
        } catch (InputMismatchException tipo) {
            System.out.println("Ingrese correctamente los datos que le solicitan.");
        }
    }

    /**
     * Esta función obtendrá todos los ejemplares que el usuario ha solicitado.
     * {@link #buscarPorID()}
     */
    public void buscarCorreo() {
        try {
            List<PrestamoDao> lista = new ArrayList<>();
            System.out.println("""
                    Buscar por corre electrónico.
                    Ingrese los datos solicitados.
                    """);
            System.out.print("Correo electrónico: ");
            String correo = sc.nextLine();

            if (!validarCorreo.validarCorreo(correo)) {
                System.out.println("Ingrese un correo electrónico valido");
            }
            //Validamos que el correo ingresado exista y sea de un usuario.
            UsuarioDTO usuario = consultaPersona.validarCredenciales(correo);
            if (usuario == null) {
                //Regresamos
                return;
            }
            //Si llega un null lo cancelamos simplemente regresando al menu inicial
            if (!usuario.getRol().equals("Bibliotecario")) {
                System.out.println("Los bibliotecarios no pueden pedir libros.");
            }
            //Ahora sabiendo que existe el correo realizamos la consulta para traer los datos de Usuario, Ejemplares y Libros
            lista = consultaPrestamo.usuarioPrestamos(correo);
            if (lista.isEmpty()) {
                //Si la lista esta vacia entonces regresamos
                return;
            }
            //Ahora imprimimos lo que hay en la lista.
            lista.forEach(list -> {
                System.out.println(list.mostrarDatos());
            });
        } catch (NullPointerException vacios) {
            System.out.println("Existe algun valor vacio");
        }


    }

    /**
     * Esta función no tiene tantas cosas, pero es la más necesaria, ya que con esta podemos saber mediante el
     * ID del prestamo obtener el estatus de algún prestamo. Esta solo obtiene un prestamo,
     * La siguiente función es similar la unica diferencia es que obtiene todos los préstamos de un usuario.
     * Lo puedes revisar aquí {@link #buscarCorreo()}
     */
    public void buscarPorID() {
        PrestamoDao prestamo = null;
        System.out.print("ID del préstamo: ");
        String ID = sc.nextLine().trim().toUpperCase();
        prestamo = consultaPrestamo.obtenerPrestamo(ID);
        if (prestamo == null) {
            //Regresamos.
            return;
        }
        System.out.println("Prestamo con ID " + ID + " encontrado.");
        System.out.println(prestamo.mostrarDatos());
        menuPrestamo();


    }

    //======================| ACTUALIZAR PRÉSTAMO (update) |================

    /**
     * Esta función sirve para regresar un ejemplar. Pero cuenta con validaciones antes de regresar el ejemplar. <br>
     * <b>Validación Prestamo</b> <br>
     * Primero se pregunta por el ID del prestamo y se realiza una busqueda, al tener el valor en el objeto
     * si el objeto es null entonces regresamos. <br><br>
     * <b>Validación entrega.</b> <br>
     * Esta validación sirve para solo se pueda realizar un cambio en préstamos que todavía no se regresan, para
     * que asi no se modifiquen préstamos que ya cumplieron.<br>
     * <br>
     * Hay otras 3 validaciones estas son para saber si se realizaron las consultas a base de datos.
     *
     */
    public void regresarLibro() {
        try {
            PrestamoDao prestamo = null;
            System.out.println("""
                    \nRegresar ejemplares.
                    Ingrese los datos que le solicitan.""");

            System.out.print("ID del préstamo: ");
            String ID = sc.nextLine().trim().toUpperCase();
            prestamo = consultaPrestamo.obtenerPrestamo(ID);
            //[MARK-Validacion-usuario]
            if (prestamo == null) {
                //Si es nulo regresamos
                System.out.println("No se encontró el préstamo ingresado.");
                return;
            }
            //Validación de préstamos no devueltos.
            Date fechaEntregado = prestamo.getFecha_real_entrega();
            if (fechaEntregado != null) {
                //Si el valor es diferente a null significa que ya se había entregado este libro, por lo que regresamos
                System.out.println("Este préstamo ya fue regresado.");
                return;
            }
            System.out.println(prestamo.mostrarDatos());
            System.out.print("""
                    \nDesea regresar el ejemplar
                    1.Devolver ejemplar.
                    2.No devolver
                    Ingrese su opción:\s""");
            int opcion = sc.nextInt();
            if (opcion != 1) {
                //Si es diferente a uno entonces regresamos
                System.out.println("No se regresara el ejemplar.");
                return;
            }
            //Primeramente, se actualizará el prestamo con la fecha en el que se entregó el ejemplar.
            Date fechaNow = fechaActual();
            //1. Primero realizaremos el cambio en prestamo
            boolean prestCambio = consultaPrestamo.updatePrestamo(ID, fechaNow);
            if (!prestCambio) {
                //Si regresa un valor significa que no se actualizó el ejemplar.
                System.out.println("No se pudo actualizar el prestamo.");
                return;
            }
            //Si procede entonces realizamos la siguiente consulta.
            //2. Actualizamos los datos del usuario
            String personaID = prestamo.getPersonaID();
            boolean usuCambio = consultaPersona.regresarEjemplar(personaID, "Sin prestamos");
            if (!usuCambio) {
                //Si regresa un false entonces es por que no se regreso el dato.
                System.out.println("No se actualizo el usuario.");
                return;
            }
            //Siguiente consulta.
            String ejemplarID = prestamo.getEjemplarID();
            //3. Actualizamos el ejemplar
            boolean ejempCambio = consultaEjemplar.cambiarEstado(ejemplarID);
            if (!ejempCambio) {
                //Si regresa un false es por que no se actualizo el ejemplar.
                System.out.println("No se actualizo ejemplares");
                return;
            }
            System.out.println("Se devolvió correctamente el ejemplar.");
            System.out.println("El usuario ahora puede otro libro.");
        } catch (InputMismatchException tipos) {
            System.out.println("Ingrese los datos que le solicitan.");
        }

    }

    public Date fechaActual() {
        LocalDate fechaNow = LocalDate.now();
        return Date.valueOf(fechaNow);
    }

}
