package Entidades;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PrestamoDao {
    //Usuario
    private String personaID;
    private String nombre;
    private String apellido;
    private String correo;
    //Libro
    private String libro;
    private String autor;
    private String categoria;
    //Ejemplar
    private String ejemplarID;
    private String fecha_prestamo;
    private Date fecha_entrega;
    private Date fecha_real_entrega;

    public PrestamoDao(String personaID, String nombre, String apellido,
                       String correo, String libro, String autor,
                       String categoria, String ejemplarID, String fecha_prestamo,
                       Date fecha_entrega, Date fecha_real_entrega) {
        this.personaID = personaID;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.libro = libro;
        this.autor = autor;
        this.categoria = categoria;
        this.ejemplarID = ejemplarID;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_entrega = fecha_entrega;
        this.fecha_real_entrega = fecha_real_entrega;
    }
    public Date getFecha_real_entrega(){
        return fecha_real_entrega;
    }
    public String getPersonaID(){
        return personaID;
    }
    public String getEjemplarID(){
        return ejemplarID;
    }

    public String mostrarDatos() {
        String diferencia = "";
        String extra = "";
        LocalDate fechaNow = LocalDate.now();
        if(fecha_real_entrega != null){
            //Si ya tiene una fecha de entrega entonces lo plasmamos en una variable
            diferencia = (fecha_real_entrega.after(fecha_entrega))?  (fecha_real_entrega + "Entregado en destiempo") : ( fecha_real_entrega + " Entregado a tiempo" );
        }else{
            diferencia = "Sin entregar. ";
            Date hoy = Date.valueOf(fechaNow);
             extra = (hoy.after(fecha_entrega))? " Entrega tardía" : " Aun puede entregar." ;
        }
        return "\n=============| Préstamo |===============" +
                "\nID del usuario: " + personaID  +
                "\nNombre: " + nombre  +
                "\nApellido: " + apellido  +
                "\nCorreo: " + correo +
                "\nLibro: " + libro +
                "\nAutor: " + autor +
                "\nCategoria: " + categoria +
                "\nEjemplarID: " + ejemplarID +
                "\nFecha préstamo: " + fecha_prestamo +
                "\nFecha entrega: " + fecha_entrega +
                "\nFecha en que se entrego: "  + diferencia + " " + extra +
                "\n=================================";
    }
}
