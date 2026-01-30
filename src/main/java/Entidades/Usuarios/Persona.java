package Entidades.Usuarios;

public abstract class Persona {
    private String personaID;
    private String nombres;
    private String apellidos;
    private String correo;
    public Persona(String personaID, String nombres, String apellidos, String correo){
        this.personaID = personaID;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public String getPersonaID(){
        return personaID;
    }
    public String getNombres(){
        return nombres;
    }
    public String getApellidos(){
        return apellidos;
    }
    public String getCorreo(){
        return correo;
    }


    public abstract String mostrarDatos();

}
