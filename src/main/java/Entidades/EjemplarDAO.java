package Entidades;

public class EjemplarDAO {
    private String ID;
    private String ISBN;
    private boolean disponible;

    public EjemplarDAO(String ID, String ISBN, boolean disponible){
        this.ID = ID;
        this.ISBN = ISBN;
        this.disponible = disponible;
    }
    public String mostrarDatos(){
        return "\nDatos fundamentales." +
                "\nCodigo ejemplar: " + ID +
                "\nCodigo libro ISBN: " + ISBN +
                "\nDisponible: " +disponible;
    }
}
