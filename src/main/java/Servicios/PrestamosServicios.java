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

    public void menuPrestamo(){
        try{
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
            switch(opcion){
                case 1 -> menuBuscar();
                case 2 -> realizarPrestamo();
                case 3 -> regresarLibro();
                case 4 -> System.out.println("Regresando el menu inicial.");
                default -> System.out.println("Ingrese una opción valida.");
            }
        }catch(InputMismatchException tipo){
            System.out.println("Ingrese los datos correctamente.");
        }
    }
    //======================| REALIZAR PRÉSTAMO (insert) |================
    public void realizarPrestamo(){
        try{
            Persona persona ;
            Ejemplar ejemplar;
            System.out.println("Realizar Prestamos" +
                    "\nIngrese los datos que se le solicitan.");

            System.out.print("Correo del usuario: ");
            String correo = sc.nextLine();
            persona = consultaPersona.correoPersona(correo);
            if(persona == null){
                //Si persona es null entonces regresamos
                return;
            }
            if(persona.getPersonaID().contains("BIB")){
                //Si contiene BIB entonces regresamos al inicio.
                System.out.println("Los bibliotecarios no pueden tomar prestados libros.");
                return;
            }
            //Validamos que el usuario no tenga deudas
            Usuario usuario = (Usuario) persona;
            String prestamo = usuario.getLibroPrestado();
            if(!prestamo.equals("Sin prestamos")){
                //Si es diferente entonces regresamos y no le prestamos a un usuario con deuda
                System.out.println("El usuario ya tiene libro prestado.");
                return;
            }
            //Como ya se realízo el filtro entonces toca validar la existencia del ejemplar
            System.out.print("ID del ejemplar: ");
            String ejemplarID = sc.nextLine().toUpperCase();
            ejemplar = consultaEjemplar.obtenerEjemplar(ejemplarID.toUpperCase());

            //Validamos si existe el ejemplar.
            if(ejemplar == null){
                //Si es igual a null entonces regresamos al inicio
                System.out.println("No se encontró el ejemplar.");
                return;
            }

            //Ahora validaremos que ese ejemplar esté disponible
            boolean disponible = ejemplar.getDisponible();
            if(!disponible){
                System.out.println("El ejemplar seleccionado no está disponible.");
                return;
            }
            //Ahora como los datos están bien validamos toca obtener la fecha del préstamo y un límite para
            //la fecha de entrega.
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //Dia del préstamo
            LocalDate fechaActual = LocalDate.now();
            String fechaPrestamo = fechaActual.format(formato);

            //Fecha del regreso del ejemplar.
            LocalDate fechaFut = fechaActual.plusWeeks(2);
            String fechaRegreso = fechaFut.format(formato);

            System.out.println("\nSe prestara el ejemplar " + ejemplar.mostrarDatos() +
                    "\nAl usuario: " + persona.mostrarDatos()  );
            //Obtenemos el ID del prestamo
            String ultimoID = consultaPrestamo.ultimoPrestamo();
            String prestamoID = "";
            if(ultimoID != null){
                //Si el valor es diferente a null entonces significa que existe algún ID
                int numeroComple = ultimoID.indexOf("_");
//                System.out.println("El _ esta en la posición:" + numeroComple + " " + ultimoID );
                String IDprest = ultimoID.substring((numeroComple + 1));
//                System.out.println("El numero es: " + IDprest);
                int numero = Integer.parseInt(IDprest);
            prestamoID = generarID.generarID("PREST", numero + 1);
            }else{
                System.out.println("Es el primer préstamo ");
                prestamoID = generarID.generarID("PREST", 1);
            }

            //Primero realizamos el insert
            boolean prestamoIn =consultaPrestamo.setPrestamo(prestamoID, ejemplarID, usuario.getPersonaID(), fechaPrestamo, fechaRegreso);
            if(prestamoIn){
                //Si se realizo el prestamo entonces mostramos una alerta.
                System.out.println("Préstamo realizado correctamente.");
            }else{
                System.out.println("No se pudo realizar el prestamo");
                return;
            }
            //Realizaremos 3 consultas la primera será para crear el prestam, la segunda para actualizar el
            // ejemplar y la tercera para actualizar al usuario
            boolean actualizarEjemplar = consultaEjemplar.cambiarEstado(ejemplarID);
            if(!actualizarEjemplar){
                //Si me regresa un false entonces regresamos y no prestamos el ejemplar;
                System.out.println("No se pudo actualizar el estado del libro.");
                return;
            }
            boolean actualizarUsuario = consultaPersona.prestarEjemplar(usuario.getPersonaID(), ejemplarID);
            if(!actualizarUsuario){
                //Si no se actualizó el usuario entonces regresamos.
                System.out.println("No se pudo guardar el ejemplar en el usuario");
                //Solamente que como no se actualizó es necesario el ejemplar es necesario restaurarlo.
                actualizarEjemplar = consultaEjemplar.cambiarEstado(ejemplarID);
                //Este if sirve para restaurar el ejemplar cuando no se actualizó el usuario.
                if(actualizarEjemplar){
                    System.out.println("El ejemplar regreso a su estado inicial.");
                }
            }
            System.out.println("Se realizo el prestamo correctamente.");
        }catch(CorreoNoValido correo){
            System.out.println(correo.getMessage());
        }
        menuPrestamo();
    }


    //======================| BUSCAR PRÉSTAMO (select) |================
    public void menuBuscar(){
        try{
            System.out.print("""
                ¿Como deseas buscar el préstamo?
                1.Correo del usuario.
                2.Por ID.
                3.Regresar al menu inicial.
                Ingrese su opción:\s""");
            int opcion = sc.nextInt();
            sc.nextLine();
            switch(opcion){
                case 1 -> buscarCorreo();
                case 2 -> buscarPorID();
                case 3 -> System.out.println("Regresando al menu inicial.");
                default -> System.out.println("Ingrese una opción valida");
            }
        }catch (InputMismatchException tipo){
            System.out.println("Ingrese correctamente los datos que le solicitan.");
        }
    }

    public void buscarCorreo(){
        try{
            List<PrestamoDao> lista = new ArrayList<>();
            System.out.println("""
                Buscar por corre electrónico.
                Ingrese los datos solicitados.
                """);
            System.out.print("Correo electrónico: ");
            String correo = sc.nextLine();

            if(!validarCorreo.validarCorreo(correo)){
                System.out.println("Ingrese un correo electrónico valido");
            }
            //Validamos que el correo ingresado exista y sea de un usuario.
            UsuarioDTO usuario = consultaPersona.validarCredenciales(correo);
            if (usuario == null) {
                //Regresamos
                return;
            }
            //Si llega un null lo cancelamos simplemente regresando al menu inicial
            if(!usuario.getRol().equals("Bibliotecario")){
                System.out.println("Los bibliotecarios no pueden pedir libros.");
            }
            //Ahora sabiendo que existe el correo realizamos la consulta para traer los datos de Usuario, Ejemplares y Libros
            lista = consultaPrestamo.usuarioPrestamos(correo);
            if(lista.isEmpty()){
                //Si la lista esta vacia entonces regresamos
                return;
            }
            //Ahora imprimimos lo que hay en la lista.
            lista.forEach(list ->{
                System.out.println(list.mostrarDatos());
            });
        }catch (NullPointerException vacios){
            System.out.println("Existe algun valor vacio");
        }


    }
    public void buscarPorID(){
        PrestamoDao prestamo = null;
        System.out.print("ID del préstamo: ");
        String ID = sc.nextLine().trim().toUpperCase();
        prestamo = consultaPrestamo.obtenerPrestamo(ID);
        if(prestamo == null){
            //Regresamos.
            return;
        }
        System.out.println("Prestamo con ID " + ID + " encontrado." );
        System.out.println(prestamo.mostrarDatos());
        menuPrestamo();


    }

    //======================| ACTUALIZAR PRÉSTAMO (update) |================

    public void regresarLibro(){
        try{
            PrestamoDao prestamo = null;
            System.out.println("""
                \nRegresar ejemplares.
                Ingrese los datos que le solicitan.""");
            System.out.print("ID del préstamo: ");
            String ID = sc.nextLine().trim().toUpperCase();
            prestamo = consultaPrestamo.obtenerPrestamo(ID);
            if(prestamo == null){
                //Si es nulo regresamos
                System.out.println("No se encontró el préstamo ingresado.");
                return;
            }
            Date fechaEntregado = prestamo.getFecha_real_entrega();
            if(fechaEntregado != null){
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
            if(opcion != 1){
                //Si es diferente a uno entonces regresamos
                System.out.println("No se regresara el ejemplar.");
                return;
            }
            //Primeramente, se actualizará el prestamo con la fecha en el que se entregó el ejemplar.
            Date fechaNow = fechaActual();
            //1. Primero realizaremos el cambio en prestamo
            boolean prestCambio = consultaPrestamo.updatePrestamo(ID, fechaNow);
            if(!prestCambio){
                //Si regresa un valor significa que no se actualizó el ejemplar.
                System.out.println("No se pudo actualizar el prestamo.");
                return;
            }
            //Si procede entonces realizamos la siguiente consulta.
            //2. Actualizamos los datos del usuario
            String personaID = prestamo.getPersonaID();
            boolean usuCambio = consultaPersona.regresarEjemplar(personaID, "Sin prestamos");
            if(!usuCambio){
                //Si regresa un false entonces es por que no se regreso el dato.
                System.out.println("No se actualizo el usuario.");
                return;
            }
            //Siguiente consulta.
            String ejemplarID = prestamo.getEjemplarID();
            //3. Actualizamos el ejemplar
            boolean ejempCambio = consultaEjemplar.cambiarEstado(ejemplarID);
            if(!ejempCambio){
                //Si regresa un false es por que no se actualizo el ejemplar.
                System.out.println("No se actualizo ejemplares");
                return;
            }
            System.out.println("Se devolvió correctamente el ejemplar.");
            System.out.println("El usuario ahora puede otro libro.");
        }catch(InputMismatchException tipos){
            System.out.println("Ingrese los datos que le solicitan.");
        }

    }

    public Date fechaActual(){
        LocalDate fechaNow = LocalDate.now();
        return Date.valueOf(fechaNow);
    }

}
