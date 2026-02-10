package Servicios;

import DAO.EjemplarDaoImpl;
import DAO.PersonaDaoImpl;
import DAO.PrestamoDaoImpl;
import Entidades.Ejemplar;
import Entidades.Usuarios.Persona;
import Entidades.Usuarios.Usuario;
import Interfaces.EjemplarDao;
import Interfaces.PersonaDAO;
import Interfaces.PrestamoDAO;
import Utilidades.GenerarID;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PrestamosServicios {

    Scanner sc = new Scanner(System.in);
    EjemplarDao consultaEjemplar = new EjemplarDaoImpl();
    PersonaDAO consultaPersona = new PersonaDaoImpl();
    PrestamoDAO consultaPrestamo = new PrestamoDaoImpl();
    GenerarID generarID = new GenerarID();


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
                case 1 -> {}
                case 2 -> realizarPrestamo();
                case 3 -> {}
                case 4 -> System.out.println("Regresando el menu inicial.");
                default -> System.out.println("Ingrese una opción valida.");
            }
        }catch(InputMismatchException tipo){
            System.out.println("Ingrese los datos correctamente.");
        }
    }
    //======================| REALIZAR PRÉSTAMO (insert) |================
    public void realizarPrestamo(){
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
        String ejemplarID = sc.nextLine();
        ejemplar = consultaEjemplar.obtenerEjemplar(ejemplarID);

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
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        //Dia del préstamo
        LocalDate fechaActual = LocalDate.now();
        String fechaPrestamo = fechaActual.format(formato);

        //Fecha del regreso del ejemplar.
        LocalDate fechaFut = fechaActual.plusWeeks(2);
        String fechaRegreso = fechaFut.format(formato);

        System.out.println("\nSe prestara el ejemplar " + ejemplar.mostrarDatos() +
                "\nAl usuario: " + persona.mostrarDatos()  );
        System.out.println("La fecha de préstamo es: " + fechaPrestamo);
        System.out.println("La fecha de regreso es: " + fechaRegreso);
        //Obtenemos el ID del prestamo
        String ultimoID = consultaPrestamo.ultimoPrestamo();
        String prestamoID = "";
        if(ultimoID != null){
            //Si el valor es diferente a null entonces significa que existe algún ID
            int numeroComple = ultimoID.indexOf("_");
            System.out.println("El _ esta en la posición: " + numeroComple + " " + ultimoID );
            String IDprest = prestamoID.substring(numeroComple, ultimoID.length());
            System.out.println("El numero es: " + IDprest);
//            prestamoID = generarID.generarID("PREST", 1);
        }else{
            System.out.println("Es el primer préstamo ");
            prestamoID = generarID.generarID("PREST", 1);
        }

        //Primero realizamos el insert
        boolean prestamoIn =consultaPrestamo.setPrestamo(prestamoID, ejemplarID, usuario.getPersonaID(), LocalDate.parse(fechaPrestamo), LocalDate.parse(fechaRegreso));
        if(prestamoIn){
            //Si se realizo el prestamo entonces mostramos una alerta.
            System.out.println("Préstamo realizado correctamente.");
            menuPrestamo();
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
        boolean actualizarUsuario = consultaPersona.prestarEjemplar(ejemplarID.toUpperCase(), usuario.getPersonaID());
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
        //Ahora realizamos la creación del prestamo.




    }


    //======================| BUSCAR PRÉSTAMO (select) |================
    public void menuBuscar(){}


    //======================| ACTUALIZAR PRÉSTAMO (update) |================

}
