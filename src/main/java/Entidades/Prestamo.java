package Entidades;

import Entidades.Usuarios.Usuario;

import java.time.LocalDate;

public class Prestamo {
    private String prestamoID;
    private Ejemplar ejemplarPrestado;
    private Usuario solicitadoPor;
    private LocalDate fechaPrestamo ;
    private LocalDate fechaEntrega;
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

    public String mostrarDatos(){
        return "";
    }




}
