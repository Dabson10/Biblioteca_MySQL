package Entidades.Usuarios;

public class Bibliotecario extends Persona{

    private String clave;

    public Bibliotecario(String personaID, String nombres, String apellidos, String correo){
        super(personaID, nombres, apellidos, correo);
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
