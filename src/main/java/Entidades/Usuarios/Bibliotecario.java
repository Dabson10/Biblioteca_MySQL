package Entidades.Usuarios;

public class Bibliotecario extends Persona{

    private String clave;

    public Bibliotecario(String personaID, String nombres, String apellidos, String correo, String clave){
        super(personaID, nombres, apellidos, correo);
        this.clave = clave;
    }

    @Override
    public String mostrarDatos(){
        return "Usuario\n" +
                "Nombre: " + getNombres() +
                "Apellido" + getApellidos() +
                "Correo: " + getCorreo() +
                "ID: " + getPersonaID() ;
    }
}
