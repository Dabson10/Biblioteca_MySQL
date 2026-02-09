package Entidades;

public class EjemplarDAO {
    private String ID;
    private String ISBN;
    private boolean disponible;
    private String ubicacion;
    private String tipo;

    public EjemplarDAO(String ID, String ISBN, boolean disponible, String ubicacion, String tipo){
        this.ID = ID;
        this.ISBN = ISBN;
        this.disponible = disponible;
        this.ubicacion = ubicacion;
        this.tipo = tipo;
    }

//    public EjemplarDAO(String ID, String ISBN, boolean disponible){
//        this.ID = ID;
//        this.ISBN = ISBN;
//        this.disponible = disponible;
//    }

    public String getID(){
        return ID;
    }
    public String getISBN(){
        return ISBN;
    }
    public String getTipo(){return tipo;}
    public String getUbicacion(){return ubicacion;}



    public String mostrarDatos(){
        return "\nDatos fundamentales." +
                "\nCodigo ejemplar: " + ID +
                "\nCodigo libro ISBN: " + ISBN +
                "\nDisponible: " +disponible +
                "\nUbicación: " + ubicacion +
                "\nTipo: " + tipo;
    }
}
