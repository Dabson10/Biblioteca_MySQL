package Entidades;

public class Ejemplar {

    private String codigoEjemplar;
    private Libro libroInfo;
    private String ubicacion;
    private String tipo; //Libro Fisico o digital.
    private boolean disponible = true;

    public Ejemplar(String codigoEjemplar, Libro libroInfo, String ubicacion, String tipo){
        if(libroInfo != null){
            this.codigoEjemplar = codigoEjemplar;
            this.libroInfo = libroInfo;
            this.ubicacion = ubicacion;
            this.tipo = tipo;
        }
    }


    public String mostrarDatos(){
        String dispo = (disponible) ? "Disponible" : "Prestado" ;
        return "Libro: " + libroInfo.mostrarDatos() +
                "\nUbicación: " + ubicacion +
                "\nTipo: " + tipo +
                "\nCodigo ejemplar: " + codigoEjemplar +
                "\nDisponible: " + dispo;
    }
}
