package Entidades;

import Entidades.Usuarios.Usuario;

import java.time.LocalDate;

public class Prestamo {
    private String prestamoID;
    private Ejemplar ejemplarPrestado;
    private Usuario solicitadoPor;
    private LocalDate fechaPrestamo ;
    private LocalDate fechaEntrega;
    private LocalDate realFechaEntrega;
    private boolean activo;

    public Prestamo(String prestamoID, Ejemplar ejemplarPrestado, Usuario solicitadoPor,
                    LocalDate fechaPrestamo, LocalDate fechaEntrega){
        if(ejemplarPrestado !=null && solicitadoPor != null){
            this.prestamoID = prestamoID;
            this.ejemplarPrestado = ejemplarPrestado;
            this.solicitadoPor = solicitadoPor;
            this.fechaPrestamo = fechaPrestamo;
            this.fechaEntrega = fechaEntrega;
        }
    }
    public void setRealFechaEntrega(LocalDate fecha){
        realFechaEntrega = fecha;
    }

    public String mostrarDatos(){
        //Esta variable sirve para saber si se entrego o no el libro.
        String entregado = (realFechaEntrega != null)? realFechaEntrega.toString() : "Aun no se entrega.";

        //extra srive para validar si la entrega fue antes o despues, si al comparar fechas es rapido,
        //pero es mejor tener una nota de que sucedio con el libro
        String extra =(realFechaEntrega.isAfter(fechaEntrega)) ? "Libro entregado a destiempo." : "Entregado a tiempo.";

        return "Libro: " + ejemplarPrestado.mostrarDatos() +
                "\nID del prestamo: " + prestamoID +
                "\nSolicitado por: " + solicitadoPor.mostrarDatos() +
                "\nFecha solicitado: " + fechaPrestamo +
                "\nFecha limite de entrega: " +
                "\nSe entrego en la fecha: " + entregado +
                extra;
    }
    
}
